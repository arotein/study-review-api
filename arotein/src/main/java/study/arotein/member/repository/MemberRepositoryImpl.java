package study.arotein.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.arotein.enumeration.Status;
import study.arotein.member.entity.Member;
import study.arotein.member.entity.TempEmail;

import javax.persistence.EntityManager;

import static study.arotein.member.entity.QMember.member;
import static study.arotein.member.entity.QTempEmail.tempEmail;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Long saveMember(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    @Override
    public Member findMemberById(Long id) {
        return queryFactory.selectFrom(member)
                .where(member.id.eq(id), member.status.ne(Status.DELETED))
                .fetchOne();
    }

    @Override
    public Member findMemberByEmail(String email) {
        return queryFactory.selectFrom(member)
                .where(member.email.eq(email), member.status.ne(Status.DELETED))
                .fetchOne();
    }

    @Override
    public void saveTempEmail(TempEmail tempEmail) {
        entityManager.persist(tempEmail);
    }

    @Override
    public TempEmail findTempEmailByApprovalStr(String approvalStr) {
        return queryFactory.selectFrom(tempEmail)
                .where(tempEmail.approvalStr.eq(approvalStr))
                .fetchOne();
    }

    @Override
    public void approvalMemberByEmail(String email) {
        queryFactory.update(member)
                .set(member.status, Status.ENABLED)
                .where(member.email.eq(email))
                .execute();
    }

    @Override
    public void removeTempEmail(TempEmail tempEmail) {
        entityManager.remove(tempEmail);
    }
}
