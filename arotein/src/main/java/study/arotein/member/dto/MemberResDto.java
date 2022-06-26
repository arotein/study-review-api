package study.arotein.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.arotein.member.entity.Member;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Setter
@Getter
@NoArgsConstructor
public class MemberResDto {
    private Long id;
    private String email;
    private String username;
    private String info;
    private Timestamp lastAccessTime;

    public MemberResDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.info = member.getInfo();
        this.lastAccessTime = member.getLastAccessTime();
    }

    public String getLastAccessTimeAsString() {
        if (lastAccessTime != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastAccessTime);
        }
        return null;
    }
}
