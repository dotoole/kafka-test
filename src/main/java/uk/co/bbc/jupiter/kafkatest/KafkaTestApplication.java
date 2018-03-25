package uk.co.bbc.jupiter.kafkatest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
@EnableDiscoveryClient
public class KafkaTestApplication {

    private static Logger logger = LoggerFactory.getLogger(KafkaTestApplication.class);

    @Autowired
    public KafkaTestApplication() {
    }


    public static void main(final String[] args) {
		SpringApplication.run(KafkaTestApplication.class, args);
	}

    @KafkaListener(topics = "test")
    public void listen(final ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("Message recieved! - {}", cr.value());
    }
}
