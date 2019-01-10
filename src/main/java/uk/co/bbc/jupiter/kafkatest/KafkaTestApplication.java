package uk.co.bbc.jupiter.kafkatest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
//@EnableDiscoveryClient
public class KafkaTestApplication {

    private static Logger logger = LoggerFactory.getLogger(KafkaTestApplication.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${ssl.truststore.location}")
    private String trustStoreLocation;

    @Value("${ssl.keystore.location}")
    private String keyStoreLocation;

    @Autowired
    public KafkaTestApplication() {
    }


    public static void main(final String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
    }

    @KafkaListener(topics = "test-naveen", groupId = "test-consumer-group")
    public void listen(final ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("Message recieved! - {}", cr.value());
    }
}
