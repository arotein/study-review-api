package study.arotein.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import study.arotein.base.BaseEntity;
import study.arotein.member.entity.Member;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/***
 * ChatRoom : ChatContents = 1 : N
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;
    private Boolean hostReadStatus = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;
    private Boolean guestReadStatus = false;

    @Column(columnDefinition = "VARCHAR(100)")
    private String latestMessage;

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatContent> chatContents = new ArrayList<>();

    @UpdateTimestamp
    private Timestamp updateTime;

    public void setLatestMessage(String latestMessage) {
        if (latestMessage.strip().length() > 100) {
            this.latestMessage = latestMessage.strip().substring(0, 100);
        } else {
            this.latestMessage = latestMessage.strip();
        }
    }
}
