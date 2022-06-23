package study.arotein.chat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.arotein.base.BaseEntity;
import study.arotein.member.entity.Member;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ChatContent extends BaseEntity {
    @Id
    @Column(name = "chat_contents_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member from;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member to;

    @Column(columnDefinition = "VARCHAR(500)")
    private String message;

    public void setMessage(String message) {
        if (message.strip().length() > 500) {
            this.message = message.strip().substring(0, 500);
        } else {
            this.message = message.strip();
        }
    }
}
