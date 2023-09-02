package com.ap4j.bma.controller.member;
// pjm - use m o p q
import com.ap4j.bma.config.PasswordEncoderConfig;
import com.ap4j.bma.model.entity.customerCenter.QnAEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.entity.member.MemberEntity;
import com.ap4j.bma.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@SessionAttributes("loginMember")   // 세션 자동 설정
@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    private MemberService qMemberService;       // 서비스 객체 생성

    @Autowired
    private PasswordEncoderConfig pwdConfig;

    /** 로그인 페이지 매핑 */
    @RequestMapping("/qLoginForm")
    public String qLoginForm(@CookieValue(value = "rememberedEmail", required = false) String rememberedEmail, Model model) {
        log.info("MemberController - qLoginForm() 실행");

        model.addAttribute("rememberedEmail", rememberedEmail); // 쿠키가 있는 경우, 저장 이메일 표시
        return "userView/oLoginForm";
    }

    /** 카카오 로그인 실행 */
    @RequestMapping("/qLogin")
    public String qLogin(@RequestParam(value = "code", required = false) String code, Model model, HttpSession session) {
        log.info("MemberController - qLogin() 실행");
        log.info("####code : " + code);

        // 1번 인증코드 요청 전달
        String accessToken = qMemberService.getAccessToken(code);     // code로 토큰 받음
        log.info("accessToken : " + accessToken);

        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = qMemberService.getUserInfo(accessToken);   // 사용자 정보 받음
        log.info("login info : " + userInfo.toString());
        log.info("login userEmail : " + userInfo.get("email"));
        log.info("login userName : " + userInfo.get("name"));
        log.info("login userNickname : " + userInfo.get("nickname"));
        log.info("login userPhone_number : " + userInfo.get("phone_number"));
        log.info("login thumbnail_image : " + userInfo.get("thumbnail_image"));

        String thumbnail_image = (String) userInfo.get("thumbnail_image");
        log.info("저장한 thumbnail_image : " + thumbnail_image);
        session.setAttribute("thumbnail_image", thumbnail_image);

        boolean emailCheck = qMemberService.existsByEmail((String) userInfo.get("email"));  // DB내 이메일 존재 여부 체크

        MemberDTO loginMember = new MemberDTO();

        if(emailCheck) {
            log.info("존재하는 이메일입니다. 로그인을 진행합니다.");    // 이미 가입된 계정이면 로그인 진행
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember = qMemberService.login(loginMember);
            loginMember.toEntity();

            model.addAttribute("loginMember",loginMember);
            log.info("loginMember : " + loginMember.toString());

        } else {
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember.setRoot(2);
            // 비밀번호 부분 수정해야함

            MemberEntity memberEntity = loginMember.toEntity();
            qMemberService.joinBasic(loginMember);   // 카카오정보로 회원가입 실행
            model.addAttribute("loginMember", loginMember);
        }
        return "redirect:/";
    }

    /** 로그아웃 */
    @RequestMapping("/qLogout")
    public String qLogout(SessionStatus sessionStatus, HttpSession session) {
        qMemberService.logout(sessionStatus, session);

        return "redirect:/";
    }

    /** 기본 로그인 */
    @PostMapping("/qLoginBasic")
    public String qBasicLogin(@ModelAttribute MemberDTO memberDTO, @RequestParam(required = false) boolean oSaveId,
                              Model model, HttpSession session, HttpServletResponse response) {
        log.info("MemberController - qBasicLogin() 실행");
        log.info("memberDTO : " + memberDTO);

        MemberDTO loginMember = qMemberService.login(memberDTO);
        if(loginMember != null) {
            session.setAttribute("errorMsg", null);
            log.info(loginMember.toString());
            log.info("로그인 성공");

            loginMember.toEntity();
            model.addAttribute("loginMember",  loginMember);

            return "redirect:/";

        } else {
             model.addAttribute("errorMsg","이메일 또는 패스워드를 다시 확인해주세요.");
            return "userView/oLoginForm";
        }
    }

    /** 기본 회원가입 폼 */
    @RequestMapping("/qJoinForm")
    public String qJoinForm() {
        return "userView/oJoinForm";
    }

    /** 기본 회원가입 */
    @PostMapping("/qJoinBasic")
    public String qJoinBasic(@ModelAttribute MemberDTO memberDTO) {
        log.info("MemberController - qJoinBasic() 실행");
        log.info("회원가입 memberDTO : " + memberDTO);
        memberDTO.setRoot(1);

        qMemberService.joinBasic(memberDTO);
        return "redirect:/member/qLoginForm";
    }

    /** 네이버 로그인 */
    @RequestMapping("/qLoginNaver")
    public String qLoginNaver(@RequestParam("code") String code,
                              @RequestParam("state") String state, Model model, HttpSession session) {
        log.info("MemberController - qLoginNaver() 실행");
        log.info("####code : " + code);

        // 1번 인증코드 요청 전달
        String accessToken = qMemberService.getAccessTokenNaver(code);     // code로 토큰 받음
        log.info("accessToken : " + accessToken);

//        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = qMemberService.getUserInfoNaver(accessToken);   // 사용자 정보 받음
        log.info("login info : " + userInfo.toString());

        String profile_image = (String) userInfo.get("profile_image");
        log.info("저장한 profile_image : " + profile_image);
        session.setAttribute("thumbnail_image", profile_image);

        boolean emailCheck = qMemberService.existsByEmail((String) userInfo.get("email"));  // DB내 이메일 존재 여부 체크
        MemberDTO loginMember = new MemberDTO();

        if(emailCheck) {
            log.info("존재하는 이메일입니다. 로그인을 진행합니다.");    // 이미 가입된 계정이면 로그인 진행
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("phone_number"));
            loginMember = qMemberService.login(loginMember);
            loginMember.toEntity();

            model.addAttribute("loginMember",loginMember);
            log.info("loginMember : " + loginMember);

        } else {
            loginMember.setEmail((String) userInfo.get("email"));
            loginMember.setName((String) userInfo.get("name"));
            loginMember.setNickname((String) userInfo.get("nickname"));
            loginMember.setTel((String) userInfo.get("mobile"));
            loginMember.setRoot(3);

            MemberEntity memberEntity = loginMember.toEntity();
            qMemberService.joinBasic(loginMember);
            model.addAttribute("loginMember", loginMember);
        }
        return "redirect:/";
    }

    /** 마이페이지 매핑 */
    @RequestMapping("/qMyPage")
    public String qMyPage(HttpSession session, Model model) {
        log.info("MemberController - qMyPage() 실행");
        String thumImg = (String) session.getAttribute("thumbnail_image");
        model.addAttribute("thumbnail_image", thumImg);
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        model.addAttribute("root", loginMember.getRoot() == 1 ? "기본회원" : loginMember.getRoot() == 2? "카카오" : "네이버");
        log.info("qMyPage에서 loginMember 세션 확인 : " + loginMember.toString());

        return "userView/myPage";
    }

    /** 내정보 수정페이지 매핑 */
    @GetMapping("/qMyInfoUpdate")
    public String qMyInfoUpdate(HttpSession session) {
        log.info("MemberController - qMyInfoUpdate() 실행");

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        log.info("qMyPage에서 loginMember 세션 확인 : " + loginMember.toString());

        return "userView/oMyInfoUpdate";
    }

    /** 내정보 수정하기 */
    @PostMapping("/qUpdateMember/{id}")
    public String qUpdate(@ModelAttribute MemberDTO updatedMember, Model model, HttpSession session) {
        log.info("MemberController - qUpdate() 실행");
        log.info("updatedMember : " + updatedMember);

        Long id =  ((MemberDTO) session.getAttribute("loginMember")).getId();

        MemberEntity memberEntity = qMemberService.updateMember(id, updatedMember);
        model.addAttribute("loginMember", memberEntity.toDTO());   // 수정 객체 지정
        log.info("회원정보 수정 완료 (수정 후) : " + memberEntity);

        return "redirect:/member/qMyPage";
    }

    /** 1:1 문의내역 페이지 매핑 */
    @GetMapping("/qMyQnA")
    public String qMyQnA(Model model) {
        List<QnAEntity> qMyQnaList = qMemberService.qMyQnaList();
        Long qMyQnaCnt = qMemberService.qMyQnaCnt();
        model.addAttribute("myQnaList", qMyQnaList);
        model.addAttribute("myQnaCnt", qMyQnaCnt);
        return "userView/myQnA";
    }

    /** 기본 회원탈퇴 */   // sns는 별도 처리 해줘야 함
    @PostMapping("/qLeaveMember/{id}")
    public String leaveMember(HttpSession session, SessionStatus sessionStatus) {
        Long id =  ((MemberDTO) session.getAttribute("loginMember")).getId();
        qMemberService.leaveMember(id,sessionStatus, session);

        return "redirect:/";
    }

    /** 이메일 중복 체크 (js ajax 활용) */
    @PostMapping("/qEmailCheck")
    @ResponseBody
    public int qEmailCheck(@RequestParam("email") String email) {
        int cnt = 0;
        boolean emailCheck = qMemberService.existsByEmail(email);
        cnt = emailCheck ? 1 : 0;
        return cnt;
    }

    /** 닉네임 중복 체크 (js ajax 활용) */
    @PostMapping("/qNicknameDuplicationCheck")
    @ResponseBody
    public int NicknameDuplicationCheck(@RequestParam("nickname") String nickname) {
        int cnt = 0;
        boolean nicknameCheck = qMemberService.existsByNickname(nickname);
        cnt = nicknameCheck ? 1 : 0;
        return cnt;
    }

    /** 이메일/비밀번호 찾기 페이지 매핑 */
    @RequestMapping("/qFindMemberInfo")
    public String qFindMemberInfo() {
        return "userView/findMemberInfo";
    }

    /** 이메일 찾기 */
    @PostMapping("/qFindEmail")
    public String qFindEmail(@RequestParam String name, @RequestParam String tel, Model model) {
        Optional<MemberEntity> findMember = qMemberService.findByNameAndTel(name, tel);

        if(findMember.isPresent()) {
            model.addAttribute("findEmail", findMember.get().getEmail());
        } else {
            model.addAttribute("findEmailFailed", "일치하는 회원정보가 없습니다.");
        }
        return "userView/findMemberInfo";
    }

    /** 비밀번혼 찾기 */
    @PostMapping("/qFindPwd")
    public String qFindPwd(@RequestParam String emailpwd, @RequestParam String telpwd, Model model) {
        Optional<MemberEntity> find = qMemberService.findByEmailAndTel(emailpwd, telpwd);
        if(find.isPresent()) {
            model.addAttribute("findPwd", find.get().getPwd());
        } else {
            model.addAttribute("findPwdFailed", "일치하는 회원정보가 없습니다.");
        }
        return "userView/findMemberInfo";
    }
}

