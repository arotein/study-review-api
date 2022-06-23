package study.arotein.init;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.arotein.chat.entity.ChatContent;
import study.arotein.chat.entity.ChatRoom;
import study.arotein.chat.repository.ChatRepository;
import study.arotein.enumeration.Role;
import study.arotein.enumeration.Status;
import study.arotein.member.entity.Member;
import study.arotein.member.repository.MemberRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class Init {
    private final InitSub initSub;

    @PostConstruct
    public void init() {
        initSub.init1();
        initSub.init2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitSub {
        private final EntityManager entityManager;
        private final PasswordEncoder passwordEncoder;
        private final MemberRepository memberRepository;
        private final ChatRepository chatRepository;

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

        public void init2() {
            createRandomChat(1L, 2L);
            createRandomChat(1L, 3L);
            createRandomChat(2L, 3L);
        }

        private void createRandomChat(Long hostId, Long guestId) {
            Member hostMember = memberRepository.findMemberById(hostId);
            Member guestMember = memberRepository.findMemberById(guestId);

            ChatRoom room = new ChatRoom();
            room.setHost(hostMember);
            room.setGuest(guestMember);

            for (int i = 0; i < 300; i++) {
                Integer randInt = Integer.parseInt(RandomStringUtils.randomNumeric(1));
                String randStr = RandomStringUtils.random(150, "안녕하세요 ㅎㅎ 여기서 랜덤하게 문자열을 추출할 계획임 ㅅㄱ");

                ChatContent content = new ChatContent();
                content.setMessage(randStr);
                if (randInt % 2 == 1) {
                    content.setFrom(hostMember);
                    content.setTo(guestMember);
                } else {
                    content.setFrom(guestMember);
                    content.setTo(hostMember);
                }
                content.setChatRoom(room);
                room.setLatestMessage(randStr);
                room.getChatContents().add(content);
            }
            chatRepository.saveChatRoom(room);
        }
    }
}
