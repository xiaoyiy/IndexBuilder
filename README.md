HW4 - Index Builder Pipeline

Preparation:
1. Run RabbitMQ with a queue named "q_product". The queue will include the crawled Ads and consumed by the Index Builder.
2. Have MySQL running, with Database "searchads" and DataTable "ad". The table schema should match the one provided by John as follow:
Columns:
adId        int(11) PK
campaignId  int(11)
keyWords    varchar(1024)
bidPrice    double
price       double
thumbnail   mediumtext
description mediumtext
brand       varchar(100)
detail_url  mediumtext
category    varchar(1024)
title       varchar(2048)
3. Have Memcached running on port 11211
4. Feed the "q_product" queue with some ad data.

Running:
To complie the IndexBuilder, make sure to include all the following jar files:
amqp-client-4.2.1.jar
jackson-annotations-2.8.3.jar
jackson-core-2.8.3.jar
jackson-databind-2.8.3.jar
json-20160807.jar
junit-4.12.jar
lucene-analyzers-common-4.0.0.jar
lucene-core-4.0.0.jar
lucene-queryparser-4.0.0.jar
mysql-connector-java-5.1.40-bin.jar
slf4j-api-1.7.25.jar
slf4j-simple-1.7.25.jar
spymemcached-2.12.1.jar

Make necessary modification to the hard-coded parameters in BuilderMain.java following line 27. Then start the IndexBuilder application to consume from "q_product" queue.

- Xiaoyi
