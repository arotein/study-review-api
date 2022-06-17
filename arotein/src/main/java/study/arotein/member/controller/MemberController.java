package study.arotein.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.arotein.base.ResponseBase;
import study.arotein.member.dto.SignUpReqDto;
import study.arotein.member.entity.Member;
import study.arotein.member.service.MemberService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseBase signUp(@RequestBody SignUpReqDto signUpReqDto) {
        Long id = memberService.signUp(signUpReqDto.getEmail(), signUpReqDto.getPassword(), signUpReqDto.getUsername());
        ResponseBase response = new ResponseBase();
        response.setData(id);
        return response;
    }

    @GetMapping("/member/{id}")
    public ResponseBase findMember(@PathVariable("id") Long id) {
        Member member = memberService.findMemberById(id);
        ResponseBase response = new ResponseBase();
        response.setData(member);
        return response;
    }
}
