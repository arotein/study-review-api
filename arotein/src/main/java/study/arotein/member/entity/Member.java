package study.arotein.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.arotein.base.BaseEntity;
import study.arotein.enumeration.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * username : unique, 3~8자의 숫자, 영문, 한글만 가능
 * email : unique
 * info : 최대 500자
 * role : ROLE_NORMAL < ROLE_MANAGER < ROLE_ADMIN 하위 권한을 포함.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"password", "username", "info"})
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    @JsonIgnore
    private String password;
    @NotNull
    @Column(unique = true, columnDefinition = "VARCHAR(8)")
    private String username;

    @Column(columnDefinition = "VARCHAR(500)")
    private String info;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    private Timestamp lastAccessTime;

    public Member(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = Role.ROLE_NORMAL;
    }

    public String getLastAccessTimeAsString() {
        if (lastAccessTime != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastAccessTime);
        }
        return null;
    }

    public String getCreateTimeAsString() {
        Timestamp createTime = super.getCreateTime();
        if (createTime != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
        }
        return null;
    }
}
