package study.arotein.member.service;

import study.arotein.member.entity.Member;

public interface MemberService {
    Long signUp(String email, String rawPassword, String username);

    Member findMemberById(Long id);
}
