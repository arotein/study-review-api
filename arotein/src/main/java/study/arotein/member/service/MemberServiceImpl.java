package study.arotein.member.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.enumeration.Status;
import study.arotein.exception.CustomException;
import study.arotein.member.dto.MemberResDto;
import study.arotein.member.entity.Member;
import study.arotein.member.entity.TempEmail;
import study.arotein.member.repository.MemberRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
    private final JavaMailSender javaMailSender;

    @Override
    public Boolean signUp(String email, String rawPassword, String username) throws MessagingException {
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

        // 이메일 인증 전 임시 계정 생성
        Member tempMember = new Member(email, passwordEncoder.encode(rawPassword), username);
        tempMember.setStatus(Status.DISABLED);
        memberRepository.saveMember(tempMember);

        // 인증 메일 전송
        return sendSignUpEmail(email);
    }

    private Boolean sendSignUpEmail(String emailAddress) throws MessagingException {
        try {
            // 이메일, 랜덤 문자열 DB에 저장
            String approvalStr = RandomStringUtils.randomAlphabetic(11);
            TempEmail tempEmail = new TempEmail(emailAddress, approvalStr);
            memberRepository.saveTempEmail(tempEmail);
            // 메일 전송
            MimeMessage email = javaMailSender.createMimeMessage();
            email.setRecipients(Message.RecipientType.TO, emailAddress);
            email.setSubject("회원가입 인증");
            email.setContent(createSignUpEmail(approvalStr), "text/html; charset=utf-8");
            javaMailSender.send(email);
        } catch (Exception e) {
            throw new CustomException(4, "메일 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
        return true;
    }

    private String createSignUpEmail(String approvalStr) {
        return String.format("다음 링크를 클릭하여 인증을 완료하세요.\n" +
                "<a href=\"http://localhost:8080/api/email/approval/%s\">인증하기</a>", approvalStr);
    }

    // 이메일 승인
    @Override
    @Modifying(clearAutomatically = true)
    public Boolean approvalEmail(String approvalStr) {
        TempEmail tempEmail = memberRepository.findTempEmailByApprovalStr(approvalStr);
        memberRepository.approvalMemberByEmail(tempEmail.getEmail());
        memberRepository.removeTempEmail(tempEmail);
        return true;
    }

    @Override
    public MemberResDto findMemberById(Long id) {
        Member member = memberRepository.findMemberById(id);
        if (member == null) {
            throw new CustomException(3, "존재하지 않는 사용자입니다.");
        }
        return new MemberResDto(member);
    }
}
