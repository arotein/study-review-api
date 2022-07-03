package study.arotein.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.arotein.base.ResponseBase;
import study.arotein.member.dto.MemberResDto;
import study.arotein.member.dto.SignUpReqDto;
import study.arotein.member.service.MemberService;

import javax.mail.MessagingException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseBase signUp(@Validated @RequestBody SignUpReqDto signUpReqDto) throws MessagingException {
        Boolean bool = memberService.signUp(signUpReqDto.getEmail(), signUpReqDto.getPassword(), signUpReqDto.getUsername());
        ResponseBase response = new ResponseBase();
        response.setData(bool);
        return response;
    }

    @GetMapping("/email/approval/{approvalStr}")
    public ResponseBase signUpApproval(@NotEmpty @PathVariable String approvalStr) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setSuccess(memberService.approvalEmail(approvalStr));
        return responseBase;
    }

    @GetMapping("/member/{id}")
    public ResponseBase findMember(@Positive @PathVariable("id") Long id) {
        MemberResDto memberDto = memberService.findMemberById(id);
        ResponseBase response = new ResponseBase();
        response.setData(memberDto);
        return response;
    }
}
