package my.test.project.dao;

import generated.test.tables.records.MessagesRecord;
import my.test.project.model.Message;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static generated.test.tables.Messages.MESSAGES;

@Component
public class KafkaDao {
    @Autowired
    private DSLContext dsl;

    public void insert(Message message) throws NonTransientDataAccessException {
        if (message != null) {
            dsl.insertInto(MESSAGES, MESSAGES.ID, MESSAGES.PAYLOAD)
               .values(message.getMessageId(), message.getPayload())
               .execute();
        }
    }

    public void insertBatch(List<Message> messages) throws NonTransientDataAccessException {
        if (messages.size() == 1) {
            insert(messages.get(0));
        } else {
            List<MessagesRecord> records = new ArrayList<>();
            for (Message message : messages) {
                records.add(new MessagesRecord(message.getMessageId(), message.getPayload()));
            }
            dsl.batchInsert(records)
               .execute();
        }
    }
}
