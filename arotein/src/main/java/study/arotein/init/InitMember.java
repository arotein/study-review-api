package study.arotein.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.enumeration.Role;
import study.arotein.enumeration.Status;
import study.arotein.member.entity.Member;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitMember {
    private final InitMemberSub initMemberSub;

    @PostConstruct
    public void init() {
        initMemberSub.init1();
    }

    @Component
    @RequiredArgsConstructor
    static class InitMemberSub {
        private final EntityManager entityManager;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public void init1() {
            Member normal = new Member("normal@arotein.com", passwordEncoder.encode("arotein123"), "normal");

            Member manager = new Member("manager@arotein.com", passwordEncoder.encode("arotein123"), "manager");
            manager.setRole(Role.ROLE_MANAGER);

            Member admin = new Member("admin@arotein.com", passwordEncoder.encode("arotein123"), "admin");
            admin.setRole(Role.ROLE_ADMIN);

            Member delete = new Member("delete@arotein.com", passwordEncoder.encode("arotein123"), "delete");
            delete.setStatus(Status.DELETED);

            Member banned = new Member("banned@arotein.com", passwordEncoder.encode("arotein123"), "banned");
            banned.setStatus(Status.BANNED);

            entityManager.persist(normal);
            entityManager.persist(manager);
            entityManager.persist(admin);
            entityManager.persist(delete);
            entityManager.persist(banned);
        }
    }
}
