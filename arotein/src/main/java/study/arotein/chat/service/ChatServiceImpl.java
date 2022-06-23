package study.arotein.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.chat.dto.ChatContentDto;
import study.arotein.chat.entity.ChatContent;
import study.arotein.chat.entity.ChatRoom;
import study.arotein.chat.repository.ChatRepository;
import study.arotein.enumeration.Status;
import study.arotein.exception.CustomException;
import study.arotein.member.entity.Member;
import study.arotein.member.repository.MemberRepository;
import study.arotein.security.bean.ClientMemberLoader;

import java.util.ArrayList;
import java.util.List;

/***
 * page는 프론트단에서는 1부터, 백단에서는 0부터 시작함
 * 예외코드  : 2001~2100
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ClientMemberLoader clientMemberLoader;

    // 새 채팅방 생성
    public Long createChatRoom(Long otherMemberId) {
        Member hostMember = clientMemberLoader.getClientMember();
        // 이미 존재하는 채팅방있으면 채팅방 id 리턴
        ChatRoom foundRoom = chatRepository.findRoomByMemberId(hostMember.getId(), otherMemberId);
        if (foundRoom != null) {
            return foundRoom.getId();
        }
        // 상대방이 유효한지 체크
        Member guestMember = memberRepository.findMemberById(otherMemberId);
        Status status = guestMember.getStatus();
        if (status != Status.ENABLED && status != Status.DISABLED) {
            throw new CustomException(2001, "존재하지 않는 사용자입니다.");
        }
        // 채팅방 없으면 생성
        ChatRoom room = new ChatRoom();
        room.setHost(hostMember);
        room.setGuest(guestMember);

        return chatRepository.saveChatRoom(room);
    }

    // 채팅방 목록, page당 10개, 마지막 1개는 다음페이지 확인용
    @Transactional(readOnly = true)
    public List<ChatRoom> loadChatRooms(Integer page) {
        return chatRepository.findRooms(clientMemberLoader.getClientMember().getId(), page);
    }

    // 채팅 내용 가져오기
    public List<ChatContentDto> loadMessages(Long chatRoomId, Integer page) {
        if (!isParticipant(chatRoomId)) {
            throw new CustomException(2002, "잘못된 접근입니다.");
        }
        Long clientMemberId = clientMemberLoader.getClientMember().getId();
        ChatRoom room = chatRepository.findRoomById(chatRoomId);
        // '읽음' 상태로 변경
        if (clientMemberId == room.getHost().getId()) {
            room.setHostReadStatus(true);
        } else {
            room.setGuestReadStatus(true);
        }

        // 요청자 본인 메세지 탐색
        List<ChatContent> chatContents = chatRepository.findChatContents(chatRoomId, page);
        List<ChatContentDto> chatContentDtos = new ArrayList<>();
        for (ChatContent content : chatContents) {
            if (clientMemberId == content.getFrom().getId()) {
                chatContentDtos.add(new ChatContentDto(content.getMessage(), content.getCreateTime(), true));
            } else {
                chatContentDtos.add(new ChatContentDto(content.getMessage(), content.getCreateTime(), false));
            }
        }
        return chatContentDtos;
    }

    // 메세지 보냈을 떄
    public void sendMessage(Long chatRoomId, String message) {
        if (!isParticipant(chatRoomId)) {
            throw new CustomException(2003, "잘못된 접근입니다.");
        }
        ChatRoom room = chatRepository.findRoomById(chatRoomId);

        ChatContent chatContent = new ChatContent();
        chatContent.setMessage(message);
        chatContent.setChatRoom(room);

        room.setLatestMessage(message);
        room.getChatContents().add(chatContent);

        // 메세지의 보낸사람, 받는사람 설정 + 채팅방 읽음 상태 설정
        Long memberId = clientMemberLoader.getClientMember().getId();
        if (memberId == room.getHost().getId()) {
            room.setHostReadStatus(true);
            room.setGuestReadStatus(false);
        } else {
            room.setHostReadStatus(false);
            room.setGuestReadStatus(true);
        }
        chatRepository.saveContent(chatContent);
    }

    // 채팅방 나가기
    public void leaveChatRoom(Long chatRoomId) {
        if (!isParticipant(chatRoomId)) {
            throw new CustomException(2004, "잘못된 접근입니다.");
        }
        chatRepository.deleteRoom(chatRoomId);
    }

    // 요청자가 채팅방의 참여자가 맞는지 확인
    private Boolean isParticipant(Long chatRoomId) {
        Long memberId = clientMemberLoader.getClientMember().getId();
        ChatRoom room = chatRepository.findRoomById(chatRoomId);
        if (memberId == room.getHost().getId() || memberId == room.getGuest().getId()) {
            return true;
        }
        return false;
    }
}
