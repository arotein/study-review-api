package study.arotein.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.arotein.base.ResponseBase;
import study.arotein.chat.dto.ChatContentDto;
import study.arotein.chat.dto.ChatContentResDto;
import study.arotein.chat.dto.ChatRoomDto;
import study.arotein.chat.dto.ChatRoomResDto;
import study.arotein.chat.service.ChatService;
import study.arotein.security.bean.ClientMemberLoader;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/***
 * page는 프론트단에서는 1부터, 백단에서는 0부터 시작함
 */
@RestController
@RequestMapping("/api/chat")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ClientMemberLoader clientMemberLoader;

    // 채팅방 목록
    @GetMapping("/rooms")
    public ResponseBase chatRooms(@Positive @RequestParam(required = false, defaultValue = "1") Integer page) {
        Long myMemberId = clientMemberLoader.getClientMember().getId();
        List<ChatRoomDto> chatRoomDtos = chatService.loadChatRooms(page - 1)
                .stream()
                .map(e -> new ChatRoomDto(e, myMemberId))
                .collect(Collectors.toList());
        ResponseBase response = new ResponseBase();
        response.setData(new ChatRoomResDto(chatRoomDtos));
        return response;
    }

    // 채팅내용 출력
    @GetMapping("/{chatRoomId}")
    public ResponseBase chatContents(@Positive @PathVariable Long chatRoomId,
                                     @Positive @RequestParam(required = false, defaultValue = "1") Integer page) {
        List<ChatContentDto> messages = chatService.loadMessages(chatRoomId, page - 1);
        ResponseBase response = new ResponseBase();
        response.setData(new ChatContentResDto(messages));
        return response;
    }

    // 채팅방 새로 생성
    @GetMapping("/create/{otherMemberId}")
    public ResponseBase startChat(@Positive @PathVariable Long otherMemberId) {
        Long roomId = chatService.createChatRoom(otherMemberId);
        ResponseBase response = new ResponseBase();
        response.setRedirect(String.format("/chat/{%d}", roomId));
        return response;
    }

    // 메세지 보내기
    @PostMapping("/{chatRoomId}/send")
    public ResponseBase sendMessage(@Positive @PathVariable Long chatRoomId,
                                    @RequestBody HashMap<String, String> messageMap) {
        chatService.sendMessage(chatRoomId, messageMap.get("message"));
        return new ResponseBase();
    }

    // 대화방 나가기
    @GetMapping("/{chatRoomId}/delete")
    public ResponseBase leaveChatRoom(@Positive @PathVariable Long chatRoomId) {
        chatService.leaveChatRoom(chatRoomId);
        ResponseBase response = new ResponseBase();
        response.setRedirect("/chat/rooms");
        return response;
    }
}
