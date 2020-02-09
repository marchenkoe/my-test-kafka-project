package my.test.project.kafka;

import lombok.extern.slf4j.Slf4j;
import my.test.project.model.IncomingMessage;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Slf4j
public class KafkaMessageDeserializer extends JsonDeserializer<IncomingMessage> {
    @Override
    public IncomingMessage deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception x) {
            log.error("Deserialize error", x);
            return null;
        }
    }

    @Override
    public IncomingMessage deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        } catch (Exception x) {
            log.error("Deserialize error", x);
            return null;
        }
    }
}