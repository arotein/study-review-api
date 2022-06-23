package study.arotein.chat.service;

import study.arotein.chat.dto.ChatContentDto;
import study.arotein.chat.entity.ChatRoom;

import java.util.List;

public interface ChatService {
    Long createChatRoom(Long otherMemberId);

    List<ChatRoom> loadChatRooms(Integer page);

    List<ChatContentDto> loadMessages(Long chatRoomId, Integer page);

    void sendMessage(Long chatRoomId, String message);

    void leaveChatRoom(Long chatRoomId);
}
