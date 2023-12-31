package com.ap4j.bma.service.member;

import com.ap4j.bma.config.PasswordEncoderConfig;
import com.ap4j.bma.model.entity.member.MailDTO;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.entity.member.MemberEntity;
import com.ap4j.bma.model.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MailService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoderConfig pwdConfig;	// 비밀번호 암호화 객체 생성

    @Autowired
    private MailSender mailSender;

    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
    public MailDTO createMailAndChangePassword(String email) {
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setEmail(email);
        dto.setTitle("아파트사조 임시비밀번호 안내 이메일 입니다.");
        dto.setMsg("안녕하세요. 아파트사조 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요. 감사합니다 :)");
        updatePassword(str,email);
        return dto;
    }

    //임시 비밀번호로 업데이트
    public void updatePassword(String userPwd, String userEmail){
        Optional<MemberEntity> member = memberRepository.findByEmail(userEmail);

        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();

            memberEntity.setPwd(pwdConfig.passwordEncoder().encode(userPwd));
            MemberDTO memberDTO = memberEntity.toDTO();
            memberDTO.updateEntity(memberEntity);

            memberRepository.save(memberEntity);
        }
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    // 메일보내기
    public void mailSend(MailDTO mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getEmail());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMsg());
        message.setFrom("alwkd920101@naver.com");
        message.setReplyTo("alwkd920101@naver.com");
        System.out.println("message"+message);
        mailSender.send(message);
    }

}
