package study.arotein.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.exception.CustomException;
import study.arotein.member.entity.Member;
import study.arotein.member.repository.MemberRepository;

/**
 * 예외코드 : 1~100
 * username 생성규칙 : 3~8자의 숫자, 영문, 한글만 가능
 * password 생성규칙 : 8~60자의 숫자, 영문 소문자, 영문 대문자, 지정 특수문자만 사용가능
 * 지정 특수문자 : ~!@#$%^&*()_+
 * username, email 중복확인 api는 생략
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(String email, String rawPassword, String username) {
        // username 검증
        String usernameRegex = "^[\\da-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{3,8}$";
        if (!username.matches(usernameRegex)) {
            throw new CustomException(1, "username은 3~8자의 숫자, 영문자, 한글만 가능합니다.");
        }

        // password 검증
        String passwordRegex = "^[\\w~!@#$%^&*()+]{8,60}$";
        if (!rawPassword.matches(passwordRegex)) {
            throw new CustomException(2, "password는 8~60자의 숫자, 영문자, 특수문자만 가능합니다.");
        }

        Member member = new Member(email, passwordEncoder.encode(rawPassword), username);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public Member findMemberById(Long id) {
        Member member = memberRepository.findMemberById(id);
        if (member == null) {
            throw new CustomException(3, "존재하지 않는 사용자입니다.");
        }
        return member;
    }
}
