package my.test.project.kafka;

import lombok.extern.slf4j.Slf4j;
import my.test.project.dao.KafkaDao;
import my.test.project.model.IncomingMessage;
import my.test.project.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

@Configuration
@ComponentScan(basePackageClasses = KafkaDao.class)
@Slf4j
public class KafkaConsumer {

    @Autowired
    KafkaDao dao;

    @KafkaListener(id = "Demo", topics = {"incomingTopic"}, containerFactory = "batchFactory")
    public void consume(List<IncomingMessage> records, Acknowledgment ack) {
        log.info("beginning to consume batch messages");
        final AtomicInteger index = new AtomicInteger(0);
        try {
            records.forEach(m -> {
                save(getCorrectMessages(m));
                index.addAndGet(1);
            });
            ack.acknowledge();
            log.info("all batch messages consumed");
        } catch (DataAccessException ex) {
            ack.nack(index.get(), 10000);
            log.error("Data access fail, rollback", ex);
        }
    }

    public void save(List<Message> messages) {
        try {
            if (messages.isEmpty()) {
                log.info("Empty messages");
                return;
            }
            dao.insertBatch(messages);
            log.info("Successfully saved {}. ", messages);
        } catch (NonTransientDataAccessException ex) {
            log.error("Save error, failed {} ({}). ", messages, ex.getMessage());
        }
    }

    public List<Message> getCorrectMessages(IncomingMessage incomingMessage) {
        if (incomingMessage != null) {
            return incomingMessage.getMessages()
                                  .stream()
                                  .filter(msg -> {
                                   if (msg != null && msg.getMessageId() > 0 && msg.getMessageId() < Integer.MAX_VALUE) {
                                       return true;
                                   }
                                   log.info("Incorrect messageId, cannot be saved {}.", msg);
                                   return false;
                               })
                                  .collect(Collectors.toList());
        } else {
            return EMPTY_LIST;
        }
    }
}
