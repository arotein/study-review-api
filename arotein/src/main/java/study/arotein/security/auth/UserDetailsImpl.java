package study.arotein.security.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import study.arotein.member.entity.Member;

import java.util.Collection;

@Getter
public class UserDetailsImpl extends User {
    private final Member member;

    public UserDetailsImpl(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities);
        this.member = member;
    }
}
