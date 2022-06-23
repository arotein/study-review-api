package study.arotein.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChatRoomResDto {
    private List<ChatRoomDto> chatRoomDtos;
    private boolean hasNext;

    public ChatRoomResDto(List<ChatRoomDto> chatRoomDtos) {
        if (chatRoomDtos.size() > 10) {
            this.hasNext = true;
            chatRoomDtos.remove(10);
        } else {
            this.hasNext = false;
        }
        this.chatRoomDtos = chatRoomDtos;
    }
}
