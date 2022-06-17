package study.arotein.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.arotein.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryDsl {
    Member save(Member member);
}
