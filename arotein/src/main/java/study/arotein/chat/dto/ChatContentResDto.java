package study.arotein.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChatContentResDto {
    private List<ChatContentDto> chatContentDtos;
    private boolean hasNext;

    public ChatContentResDto(List<ChatContentDto> chatContentDtos) {
        if (chatContentDtos.size() > 50) {
            this.hasNext = true;
            chatContentDtos.remove(50);
        } else {
            this.hasNext = false;
        }
        this.chatContentDtos = chatContentDtos;
    }
}
