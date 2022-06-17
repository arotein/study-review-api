package study.arotein.member.repository;

import study.arotein.member.entity.Member;

public interface MemberRepositoryDsl {
    Member findMemberById(Long id);
}
