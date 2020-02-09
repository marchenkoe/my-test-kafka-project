package my.test.project.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Message {
    @Min(0)
    long messageId;
    @NotNull
    String payload;
}
