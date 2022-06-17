package study.arotein.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.arotein.enumeration.Status;
import study.arotein.member.entity.Member;

import javax.persistence.EntityManager;

import static study.arotein.member.entity.QMember.member;

@Repository
public class MemberRepositoryDslImpl implements MemberRepositoryDsl {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberRepositoryDslImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Member findMemberById(Long id) {
        return queryFactory.selectFrom(member)
                .where(member.id.eq(id), member.status.ne(Status.DELETED))
                .fetchOne();
    }
}
