import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import com.alibaba.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

public class mysql_flink_kafka {

    public static DataStreamSource<String> tmp = null;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Set up environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        // Connect to MySQL
        DebeziumSourceFunction<String> mySqlSource = MySQLSource.<String>builder()
                .hostname("localhost")
                .port(3306)
                .databaseList("test")
                .tableList("test.user_view")
                .username("root")
                .password("xxx")
                .deserializer(new StringDebeziumDeserializationSchema())
                .startupOptions(StartupOptions.initial())
                .build();

        // Get data stream from MySQL to Flink and print
        DataStreamSource<String> dataStreamSource = env.addSource(mySqlSource);
        dataStreamSource.print();

        // Kafka topic
        String outputTopic = "output-demo";
        String server = "localhost:9092";

        // Sending data stream to Kafka
        StreamSender(outputTopic, server, env, dataStreamSource);

        env.execute();
    }

    /**
     * 
     * @param outputTopic
     * @param server
     * @param env
     * @param dataStreamSource
     * @throws Exception
     */
    public static void StreamSender(String outputTopic, String server, StreamExecutionEnvironment env, DataStreamSource<String> dataStreamSource) throws Exception {

        DataStream<String> stringOutputStream = dataStreamSource.process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, Context ctx, Collector<String> out) {
                // Process each element within the stream
                String newVal = value.toUpperCase();
                System.out.println("From Flink : "+ newVal);
                out.collect(newVal);
            }
        });

        FlinkKafkaProducer<String> flinkKafkaProducer = createStringProducer(outputTopic, server);

        stringOutputStream.addSink(flinkKafkaProducer);
    }

    /**
     * Kafka producer: creating message within a specified topic for the customers to view
     * @param topic
     * @param kafkaAddress
     * @return
     */
    public static FlinkKafkaProducer<String> createStringProducer(
            String topic, String kafkaAddress){

        return new FlinkKafkaProducer<>(kafkaAddress,
                topic, new SimpleStringSchema());
    }
}
