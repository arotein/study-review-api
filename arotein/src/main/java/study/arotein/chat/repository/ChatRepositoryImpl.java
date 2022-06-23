package study.arotein.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.arotein.chat.entity.ChatContent;
import study.arotein.chat.entity.ChatRoom;
import study.arotein.enumeration.Status;

import javax.persistence.EntityManager;
import java.util.List;

import static study.arotein.chat.entity.QChatContent.chatContent;
import static study.arotein.chat.entity.QChatRoom.chatRoom;

@Repository
public class ChatRepositoryImpl implements ChatRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ChatRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long saveChatRoom(ChatRoom chatRoom) {
        entityManager.persist(chatRoom);
        return chatRoom.getId();
    }

    public void saveContent(ChatContent chatContent) {
        entityManager.persist(chatContent);
    }

    // 채팅방
    public ChatRoom findRoomById(Long chatRoomId) {
        return queryFactory.selectFrom(chatRoom)
                .join(chatRoom.host).fetchJoin()
                .join(chatRoom.guest).fetchJoin()
                .where(chatRoom.id.eq(chatRoomId))
                .fetchOne();
    }

    // 채팅방
    public ChatRoom findRoomByMemberId(Long fromMemberId, Long toMemberId) {
        Long[] ids = {fromMemberId, toMemberId};
        return queryFactory.selectFrom(chatRoom)
                .where(chatRoom.host.id.in(ids),
                        chatRoom.guest.id.in(ids))
                .fetchOne();
    }

    // 채팅 내용
    public List<ChatContent> findChatContents(Long chatRoomId, Integer page) {
        return queryFactory.selectFrom(chatContent)
                .join(chatContent.from).fetchJoin()
                .where(chatContent.chatRoom.id.eq(chatRoomId),
                        chatContent.status.eq(Status.ENABLED))
                .orderBy(chatContent.createTime.desc())
                .limit(51)
                .offset(50 * page)
                .fetch();
    }

    // 채팅방 목록
    public List<ChatRoom> findRooms(Long hostOrGuestId, Integer page) {
        return queryFactory.selectFrom(chatRoom)
                .join(chatRoom.host).fetchJoin()
                .join(chatRoom.guest).fetchJoin()
                .where(chatRoom.host.id.eq(hostOrGuestId)
                        .or(chatRoom.guest.id.eq(hostOrGuestId)))
                .orderBy(chatRoom.updateTime.desc())
                .limit(11)
                .offset(10 * page)
                .fetch();
    }

    // 채팅방 및 채팅 내용 삭제
    public void deleteRoom(Long chatRoomId) {
        ChatRoom room = findRoomById(chatRoomId);
        entityManager.remove(room);
    }
}
