package study.arotein;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.arotein.security.bean.ClientMemberLoader;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelloController {

    private final ClientMemberLoader clientMemberLoader;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/auth")
    public Object auth() {
        if (clientMemberLoader.isAnonymous()) {
            return "Anonymous유저";
        }
        return clientMemberLoader.getClientMember();
    }

    @PreAuthorize("hasRole('NORMAL')")
    @RequestMapping("/normal")
    public String normalTest() {
        return "normal access test OK";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping("/manager")
    public String managerTest() {
        return "manager access test OK";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public String adminTest() {
        return "admin access test OK";
    }
}
