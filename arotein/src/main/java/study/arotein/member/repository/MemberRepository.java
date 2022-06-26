package study.arotein.member.repository;

import study.arotein.member.entity.Member;
import study.arotein.member.entity.TempEmail;

public interface MemberRepository {
    Long saveMember(Member member);

    Member findMemberById(Long id);

    Member findMemberByEmail(String email);

    void saveTempEmail(TempEmail tempEmail);

    TempEmail findTempEmailByApprovalStr(String randStr);

    void approvalMemberByEmail(String email);

    void removeTempEmail(TempEmail email);
}
