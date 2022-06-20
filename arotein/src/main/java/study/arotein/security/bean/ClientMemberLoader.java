package study.arotein.security.bean;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import study.arotein.member.entity.Member;
import study.arotein.security.auth.UserDetailsImpl;

/***
 * 로그인 상태 : 클라이언트의 계정 정보를 리턴
 * 비로그인 상태 : Member객체대신 null을 리턴
 */
@Component
public class ClientMemberLoader {
    public Member getClientMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
        }
        return null;
    }

    public boolean isAnonymous() {
        if (getClientMember() == null) {
            return true;
        }
        return false;
    }
}
