package uk.co.bbc.jupiter.kafkatest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
@EnableDiscoveryClient
public class KafkaTestApplication {

    @Autowired
    public KafkaTestApplication() {
    }


    public static void main(final String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
    }


}
