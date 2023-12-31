package com.ap4j.bma.controller.talktalk;


import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import com.ap4j.bma.model.entity.apt.AptEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.service.apartment.ApartmentService;
import com.ap4j.bma.service.apartment.ApartmentServiceImpl;
import com.ap4j.bma.service.talktalk.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@SessionAttributes("loginMember")
@Controller
public class TalkTalkReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ApartmentService aptService;
    @Autowired
    ApartmentServiceImpl aptServiceImpl; // 아파트 서비스 주입
    @Autowired
    private AptEntity aptEntity;



    //맵컨트롤러에서 출력된다 주석처리
//    @GetMapping("/board/list")
//    public String reviewList(Model model){
//        List<TalkTalkReviewEntity> list = reviewService.reviewList();
//        log.info("리뷰컨트롤러 리뷰리스트반환~"+list.toString());
//
//        //서비스에서 생성한 리스트를 list라는 이름으로 반환하겠다.
//        model.addAttribute("list", list);
//        return "reviewlist";
//    }

//    @GetMapping("/board/writepro")
//    public String boardwritePro(@RequestParam("id")Long apartmentId){
//        log.info("아이다값이 들어오는 메서드 실행되"+apartmentId);
//
//        return "/board/writepro";
//    }

    @PostMapping("/board/writepro")
    public String boardwritePro(@RequestParam("content") String content,
                                @RequestParam(value = "id", defaultValue = "0") Object apartmentId, HttpSession session){

        log.info("리뷰컨트롤러 boardWritePro실행, content: " + content);

        // boardwritePro2 메서드를 호출하여 아파트 아이디를 가져옴
        //ResponseEntity<Long> responseEntity = boardwritePro2(apartmentId);
        //String apartmentIdString = String.valueOf(responseEntity.getBody());

        apartmentId = session.getAttribute("id");
        // 아파트 아이디를 long 타입으로 파싱
//        Long returnedApartmentId = Long.parseLong(apartmentIdString);

        System.out.println(apartmentId instanceof Long);
        System.out.println(apartmentId instanceof Object);


        // 아파트 아이디가 null이 아닌지 확인

        boolean res = content != null && apartmentId != "0";
        System.out.println("결과: "+ res);

        if (res)  {
            TalkTalkReviewEntity reviewEntity = new TalkTalkReviewEntity();
            reviewEntity.setId((Long)apartmentId); // 아파트 아이디 설정
            reviewEntity.setBoard_no(99);

            MemberDTO dto = (MemberDTO) session.getAttribute("loginMember");
//            session.setAttribute("id",apartmentId);
            String email = dto.getEmail();
            reviewEntity.setEmail(email);

            reviewEntity.setContent(content);

            reviewService.write(reviewEntity);
            log.info("리뷰작성, 저장이 완료되었다~"+reviewEntity.toString());
        } else {
            log.error("아파트 아이디가 올바르지 않습니다.");
        }


//        reviewEntity.setBoard_no(99);
//
//        MemberDTO dto = (MemberDTO) session.getAttribute("loginMember");
//        String email = dto.getEmail();
//        reviewEntity.setEmail(email);
//
//        reviewEntity.setContent(content);
//
//        reviewService.write(reviewEntity);
//        log.info(reviewEntity.toString());


        return "redirect:/map/main";
    }


    @PostMapping("/board/writepro/{id}")
    @ResponseBody
    public ResponseEntity<Long> boardwritePro2(@PathVariable("id") Long apartmentId,HttpSession session) {
        log.info("리뷰컨트롤러 boardWritePro실행, id: " + apartmentId);
        session.setAttribute("id",apartmentId);
//        //세션값 받기, 새로운 새션값 들어오면 기존 거 삭제. =!
//        //리턴값 필요없거나 아이디값만 넘겨. 세ㅔ션에 저ㅏㅇ되니까. 빌드메서드(바디가 비어있다는)
//        httpservletReqeust res = null;
//        httpseesein see = req.getsseigon()
//        if (session == null) {
//
//        }
//        req.setattrivute("id",apartmentId);
//
//        if(apartmentId)

        // 아파트 아이디를 ResponseEntity로 감싸서 리턴
        return new ResponseEntity<>(apartmentId, HttpStatus.OK);
    }







}
