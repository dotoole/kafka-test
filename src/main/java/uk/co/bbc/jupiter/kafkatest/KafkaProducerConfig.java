package uk.co.bbc.jupiter.kafkatest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import uk.co.bbc.jupiter.nt.common.messaging.config.MessageProducerConfig;
import uk.co.bbc.jupiter.nt.model.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Primary
public class KafkaProducerConfig extends MessageProducerConfig {

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        @Value("${spring.kafka.producer.retries}")
        private Integer retries;

        @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
        private Integer maxInFlightRequests;

        @Value("${spring.kafka.producer.properties.retry.backoff.ms}")
        private Integer retryBackoff;

        @Value("${spring.kafka.topic.produce.to}")
        private String topicName;

        @Value("${ssl.truststore.location}")
        private String trustStoreLocation;

        @Value("${ssl.keystore.location}")
        private String keyStoreLocation;

        public KafkaProducerConfig() {
        }

        @Bean
        public ProducerFactory<String, Message> producerFactory() {
                DefaultKafkaProducerFactory<String, Message> producerFactory = new DefaultKafkaProducerFactory(this.producerConfigs());
                producerFactory.setValueSerializer(this.jsonSerializer());
                return producerFactory;
        }

        @Bean
        public KafkaTemplate<String, Message> kafkaTemplate() {
                return new KafkaTemplate(this.producerFactory());
        }

        @Bean
        public ObjectMapper getObjectMapper() {
                return Jackson2ObjectMapperBuilder.json().modules(new Module[]{new JavaTimeModule()}).featuresToDisable(new Object[]{ SerializationFeature.WRITE_DATES_AS_TIMESTAMPS}).serializationInclusion(
                        JsonInclude.Include.NON_NULL).build();
        }

        private JsonSerializer<Message> jsonSerializer() {
                ObjectMapper customObjectMapper = Jackson2ObjectMapperBuilder.json().modules(new Module[]{new JavaTimeModule()}).featuresToDisable(new Object[]{SerializationFeature.WRITE_DATES_AS_TIMESTAMPS}).build();
                return new JsonSerializer(customObjectMapper);
        }

        private Map<String, Object> producerConfigs() {
                Map<String, Object> props = new HashMap();
                props.put("bootstrap.servers", this.bootstrapServers);
                props.put("key.serializer", StringSerializer.class);
                props.put("retries", this.retries);
                props.put("retry.backoff.ms", this.retryBackoff);
                props.put("max.in.flight.requests.per.connection", this.maxInFlightRequests);
                props.put(ProducerConfig.ACKS_CONFIG, "all");
                props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
                props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
                props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                props.put("metadata.broker.list",bootstrapServers);
                props.put("security.protocol", "SSL");
                props.put("ssl.truststore.location", trustStoreLocation);
                props.put("ssl.truststore.password", "confluent");

                props.put("ssl.key.password", "confluent");
                props.put("ssl.keystore.password", "confluent");
                props.put("ssl.keystore.location", keyStoreLocation);

                return props;
        }

        public String getTopicName() {
                return this.topicName;
        }
}
