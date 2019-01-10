package uk.co.bbc.jupiter.kafkatest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import uk.co.bbc.jupiter.nt.common.messaging.MessageProducer;
import uk.co.bbc.jupiter.nt.common.messaging.config.MessageConfig;
import uk.co.bbc.jupiter.nt.model.messaging.Data;
import uk.co.bbc.jupiter.nt.model.messaging.Entity;
import uk.co.bbc.jupiter.nt.model.messaging.Envelope;
import uk.co.bbc.jupiter.nt.model.messaging.Header;
import uk.co.bbc.jupiter.nt.model.messaging.Message;
import uk.co.bbc.jupiter.nt.model.messaging.Payload;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Import({ MessageConfig.class, MessageProducer.class })
public class KafkaProducer {
        private final ObjectMapper objectMapper;
        private final MessageProducer messageProducer;

        public KafkaProducer(ObjectMapper objectMapper, MessageProducer messageProducer) {
                this.objectMapper = objectMapper;
                this.messageProducer = messageProducer;
        }

        @PostConstruct
        public void send() throws IOException {
                Message message = new Message();
                Header header = new Header();
                header.setCorrelationID("naveen123");
                message.setHeader(header);
                Envelope envelope = new Envelope();
                Payload payload = convertToPayload(envelope);
                Data data = new Data();
                data.setPayload(payload);
                message.setData(data);
                messageProducer.send(message);
        }

        private Payload convertToPayload(Envelope payload) throws IOException {
                String payloadJson = objectMapper.writeValueAsString(payload);
                return objectMapper.readValue(payloadJson, Payload.class);
        }

        private <T> Entity convertToEntity(T entity) throws IOException {
                String entityJson = objectMapper.writeValueAsString(entity);
                return objectMapper.readValue(entityJson, Entity.class);
        }

}
