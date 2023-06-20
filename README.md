# MySQL_Flink_Kafka

## Environment Setup
- IDE: IntelliJ IDEA (2019.3.5)
    - Create a Maven Project: using maven-archetype-quickstart:1.4
    - Change `pom.xml`: adding dependencies corresponding to the version of your flink and kafka
        > https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/connectors/datastream/kafka/
        > https://github.com/ververica/flink-cdc-connectors
        ```
          <properties>
            <java.version>1.8</java.version>
            <scala.version>2.12</scala.version>
            <maven.compiler.source>${java.version}</maven.compiler.source>
            <maven.compiler.target>${java.version}</maven.compiler.target>
            <hadoop.version>3.1.3</hadoop.version>
            <flink.version>1.13.1</flink.version>
            <flink.connector>1.13.6</flink.connector>
        </properties>
        <dependencies>
            ...
        </dependencies>
        ```
- MySQL Installation
    - Install MySQL: 
        - [Tutorial](https://www.youtube.com/watch?v=UcpHkYfWarM)
    - MySQL IDE: Navicat Premium
        - [How to connect Navicat with MySQL server](https://www.youtube.com/watch?v=Ky-CGWMbdqw)
- Kafka Documentation(Version: kafka_2.11-2.4.0)
    - Starting Kafka Zookeeper: 
        > bin/zookeeper-server-start.sh config/zookeeper.properties
    - Starting Kafka broker: 
        > bin/kafka-server-start.sh config/server.properties
    - Creating Kafka Topic: 
        > bin/kafka-topics.sh --create --topic cities --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
    - Producing messages: 
        > bin/kafka-console-producer.sh --broker-list localhost:9092 --topic cities 
    - Consuming messages: 
        > bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cities (--from-beginning)
    - Listing all the topics
        > ./kafka-topics.sh --list --bootstrap-server localhost:9092

- Flink Documentation (Version: flink-1.13.1)
    - Start running: ./bin/start-cluster.sh
    - Stop running: ./bin/stop-cluster.sh

