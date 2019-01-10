package uk.co.bbc.jupiter.kafkatest;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableKafka
public class KafkaJupiterConsumer {
        private static Logger logger = LoggerFactory.getLogger(KafkaJupiterConsumer.class);

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServer;

        @Value("${jupiter.kafka.consumer.group-id}")
        private String groupId;
        @Value("${ssl.truststore.location}")
        private String trustStoreLocation;

        @Value("${ssl.keystore.location}")
        private String keyStoreLocation;

        @Value(("${jupiter.kafka.topic}"))
        private String kafkaTopic;


        @KafkaListener(topics = "${spring.kafka.topic.produce.to}", groupId = "${jupiter.kafka.consumer.group-id}")
        public void listen(final ConsumerRecord<?, ?> cr) throws Exception {
                logger.info("Message recieved! - {}", cr.value());
        }

        @Scheduled(fixedDelay = 6000)
        public void readMessages() {
                Map<String, Object> props = new HashMap<>();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                props.put(ConsumerConfig.CLIENT_ID_CONFIG, "naveen-local-machine");
                props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
                props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 60000);
                props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
                props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
                props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                props.put("security.protocol", "SSL");
                props.put("ssl.truststore.location", trustStoreLocation);
                props.put("ssl.truststore.password", "confluent");

                props.put("ssl.key.password", "confluent");
                props.put("ssl.keystore.password", "confluent");
                props.put("ssl.keystore.location", keyStoreLocation);

                ConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(props);
                Consumer<Integer, String> consumer = cf.createConsumer();

                //KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
                try {
                     //consumer.subscribe(Arrays.asList(kafkaTopic));
                     consumer.assign(Lists.newArrayList(new TopicPartition(kafkaTopic, 0)));
                        ConsumerRecords<Integer, String> records = consumer.poll(100);
                        System.err.println("records size=>"+records.count());
                     for (ConsumerRecord<Integer, String> record : records)
                            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                } catch (Exception ex){
                   ex.printStackTrace();
                } finally {
                   consumer.close();
                }
        }
}
