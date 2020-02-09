package my.test.project.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IncomingMessage {
    @Valid
    @NotNull
    List<Message> messages;
}
