package study.arotein.chat.dto;

import lombok.Getter;
import lombok.Setter;
import study.arotein.chat.entity.ChatRoom;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Setter
@Getter
public class ChatRoomDto {
    private Long roomId;
    private String otherMemberUsername;
    private Boolean myReadStatus;
    private Timestamp updateTime;
    private Timestamp updateTimeAsString;
    private String latestMessage;

    public ChatRoomDto(ChatRoom chatRoom, Long myMemberId) {
        if (myMemberId == chatRoom.getHost().getId()) {
            this.otherMemberUsername = chatRoom.getGuest().getUsername();
            this.myReadStatus = chatRoom.getHostReadStatus();
        } else {
            this.otherMemberUsername = chatRoom.getHost().getUsername();
            this.myReadStatus = chatRoom.getGuestReadStatus();
        }
        this.roomId = chatRoom.getId();
        this.updateTime = chatRoom.getUpdateTime();
        this.updateTimeAsString = chatRoom.getUpdateTime();
        this.latestMessage = chatRoom.getLatestMessage();
    }

    public String getUpdateTimeAsString() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat compDate = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String simpleNow = compDate.format(now);
        String simpleUpdateTime = compDate.format(updateTime);

        if (updateTime == null) {
            return null;
        }
        // 올해가 아닐 때 : yyyy년 MM월 dd일
        if (!simpleNow.substring(0, 5).equals(simpleUpdateTime.substring(0, 5))) {
            return simpleUpdateTime.substring(0, 13);
        }
        // 올해이고 오늘이 아닐 때 : MM월 dd일
        if (!simpleNow.substring(6, 13).equals(simpleUpdateTime.substring(6, 13))) {
            return simpleUpdateTime.substring(6, 13);
        }
        // 올해이고 오늘이고 시, 분이 다를 때 : HH시 mm분
        if (!simpleNow.substring(14, 21).equals(simpleUpdateTime.substring(14, 21))) {
            return simpleUpdateTime.substring(14, 21);
        }
        // 그 외 : 방금
        return "방금";
    }
}
