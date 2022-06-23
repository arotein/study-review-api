package study.arotein.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class ChatContentDto {
    private String message;
    private Timestamp sendTime;
    private Boolean me;
}
