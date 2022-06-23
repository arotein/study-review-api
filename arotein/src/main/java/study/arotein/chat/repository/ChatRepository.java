package study.arotein.chat.repository;

import study.arotein.chat.entity.ChatContent;
import study.arotein.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRepository {
    Long saveChatRoom(ChatRoom chatRoom);

    void saveContent(ChatContent chatContent);

    ChatRoom findRoomById(Long chatRoomId);

    List<ChatContent> findChatContents(Long chatRoomId, Integer page);

    List<ChatRoom> findRooms(Long hostOrGuestId, Integer page);

    ChatRoom findRoomByMemberId(Long fromMemberId, Long toMemberId);

    void deleteRoom(Long chatRoomId);
}
