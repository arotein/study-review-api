package study.arotein.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.enumeration.Status;
import study.arotein.exception.CustomAuthenticationException;
import study.arotein.member.entity.Member;
import study.arotein.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

/***
 * 예외코드 : 1101~1200
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email);
        // 계정 검증
        if (member == null) {
            throw new CustomAuthenticationException(1101, HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 계정입니다.");
        }
        if (member.getStatus() == Status.BANNED) {
            throw new CustomAuthenticationException(1102, HttpStatus.UNAUTHORIZED.value(), "정지된 계정입니다.");
        }
        if (member.getStatus() == Status.DISABLED) {
            throw new CustomAuthenticationException(1103, HttpStatus.UNAUTHORIZED.value(), "일시적으로 비활성화된 계정입니다.");
        }
        // 계정 권한
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getRole().toString()));

        return new UserDetailsImpl(member, roles);
    }
}
