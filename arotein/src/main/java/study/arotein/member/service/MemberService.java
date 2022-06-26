package study.arotein.member.service;

import study.arotein.member.dto.MemberResDto;

import javax.mail.MessagingException;

public interface MemberService {
    Boolean signUp(String email, String rawPassword, String username) throws MessagingException;

    Boolean approvalEmail(String approvalStr);

    MemberResDto findMemberById(Long id);
}
