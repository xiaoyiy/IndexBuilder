package io.bittiger.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.bittiger.ad.Ad;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BuilderMain {
    private final static String IN_QUEUE_NAME = "q_product";

    private static IndexBuilder indexBuilder;
    private static ObjectMapper mapper;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        mapper = new ObjectMapper();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel inChannel = connection.createChannel();
        inChannel.queueDeclare(IN_QUEUE_NAME, true, false, false, null);
        inChannel.basicQos(10); // Per consumer limit
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // Please adjust the parameters accordingly
        String memcachedServer = "127.0.0.1";
        int memcachedPortal = 11211;
        String mysql_host = "127.0.0.1:3306";
        String mysql_db = "searchads";
        String mysql_user = "root";
        String mysql_pass = "root";
        indexBuilder = new IndexBuilder(memcachedServer, memcachedPortal, mysql_host, mysql_db, mysql_user, mysql_pass);

        //callback
        Consumer consumer = new DefaultConsumer(inChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    Ad ad = mapper.readValue(body, Ad.class);
                    System.out.println(" [x] Received Ad Id: " + ad.adId);

                    if (!indexBuilder.buildInvertIndex(ad) || !indexBuilder.buildForwardIndex(ad)) {
                        //log
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        inChannel.basicConsume(IN_QUEUE_NAME, true, consumer);
    }
}
