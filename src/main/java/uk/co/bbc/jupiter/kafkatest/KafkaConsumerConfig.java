package uk.co.bbc.jupiter.kafkatest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServer;

        @Value("${jupiter.kafka.consumer.group-id}")
        private String groupId;
        @Value("${ssl.truststore.location}")
        private String trustStoreLocation;

        @Value("${ssl.keystore.location}")
        private String keyStoreLocation;


        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
                Map<String, Object> props = new HashMap<>();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, 1);
                props.put(ConsumerConfig.CLIENT_ID_CONFIG, 123);
                props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
                props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 60000);
                props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
                props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
                //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offSet);
                props.put("security.protocol", "SSL");
                props.put("ssl.truststore.location", trustStoreLocation);
                props.put("ssl.truststore.password", "confluent");

                props.put("ssl.key.password", "confluent");
                props.put("ssl.keystore.password", "confluent");
                props.put("ssl.keystore.location", keyStoreLocation);

                return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
                ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(consumerFactory());
                return factory;
        }
}
