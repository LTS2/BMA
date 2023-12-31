package com.ap4j.bma.controller.maemulReg;

import com.ap4j.bma.model.entity.meamulReg.MaemulPhotoEntity;
import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.repository.MaemulPhotoRepository;
import com.ap4j.bma.service.maemulReg.MaemulPhotoService;
import com.ap4j.bma.service.maemulReg.MaemulRegService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;


@SessionAttributes({"loginMember", "maemulRegEntity"})
@Controller
@Slf4j
@RequestMapping("/maemul")
public class MaemulRegController {

    @Autowired
    private MaemulRegService maemulRegService;
    @Autowired
    private MaemulPhotoRepository maemulPhotoRepository;
    @Autowired
    private MaemulPhotoService maemulPhotoService;

    //약관동의 페이지
    @GetMapping("/agree")
    public String agreementForm(HttpSession session) {
        MemberDTO memberDTO = (MemberDTO) session.getAttribute("loginMember");
        if(memberDTO == null){
            return "userView/loginNeed";
        }

        return "maemulReg/agree";
    }

    //매물정보 입력 페이지
    @GetMapping("/maemulinfo")
    public String maemulinfoForm(Model model) {
        model.addAttribute("maemulRegEntity", new MaemulRegEntity());
        return "maemulReg/maemulInfo";
    }

    //매뮬정보저장
    @PostMapping("/maemulinfo")
    public String saveMaemulInfo(@ModelAttribute MaemulRegEntity maemulRegEntity, Model model) {
        // 매물 정보를 임시로 세션에 저장
        model.addAttribute("maemulRegEntity", maemulRegEntity);
        return "redirect:/maemul/moreinfo";
    }

    // 상세 정보 입력 페이지
    @GetMapping("/moreinfo")
    public String moreinfoForm(Model model) {
        if (!model.containsAttribute("maemulRegEntity")) {
            return "redirect:/maemulinfo"; // 세션에 매물 정보가 없으면 다시 입력 페이지로 이동
        }
        return "maemulReg/moreinfo";
    }

    @Transactional
    @PostMapping("/moreinfo")
    public String saveMoreInfo(@ModelAttribute MaemulRegEntity maemulRegEntity,
                               @RequestParam("imageFiles") MultipartFile[] imageFiles,
                               RedirectAttributes redirectAttributes) throws Exception {
        // 매물 정보를 데이터베이스에 저장
        MaemulRegEntity savedEntity = maemulRegService.saveMaemulInfo(maemulRegEntity);

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                // 새로운 MaemulPhotoEntity 인스턴스 생성
                MaemulPhotoEntity maemulPhotoEntity = new MaemulPhotoEntity();

                // 매물 ID 설정 및 엔티티 저장
                maemulPhotoEntity.setMaemulID(savedEntity.getId());

                // 이미지를 서버로 업로드하고 데이터베이스에 저장하는 로직 추가
                maemulPhotoService.saveImage(file, maemulPhotoEntity);

                // 데이터베이스에 저장
                maemulPhotoRepository.save(maemulPhotoEntity);
            }
        }

        // 저장된 매물 정보의 ID를 리다이렉트 시에 전달
        redirectAttributes.addAttribute("maemulId", savedEntity.getId());


        return "redirect:/maemul/saveCoordinates";
    }
    // 매물 좌표 저장
    @GetMapping("/saveCoordinates")
    public String maemulSaveCoordinates(@RequestParam("maemulId") Integer maemulId, HttpSession session) {
        MaemulRegEntity maemulRegEntity = maemulRegService.getMaemulById(maemulId);
        session.setAttribute("maemulRegEntity", maemulRegEntity);
        return "redirect:/member/qManagement";
    }

    @PostMapping("/saveCoordinates")
    public void maemulSaveCoordinates(Integer maemulId, Double latitude, Double longitude) {
        maemulRegService.updateMeamulReg(maemulId, latitude, longitude);
    }

    @PostMapping("/update-maemul")
    public String setUpdateMaemul(@ModelAttribute MaemulRegEntity maemulRegEntity) {
        System.out.println("maemulRegEntity = " + maemulRegEntity);
        MaemulRegEntity updateMaemul = maemulRegService.updateMaemul(maemulRegEntity);
        if (updateMaemul != null) {
            // 업데이트 성공한 경우
            System.out.println("updateMaemul = " + updateMaemul);
        } else {
            // 업데이트 실패한 경우
            System.out.println("업데이트 실패");
        }
        return "redirect:/member/qManagement";
    }

    @GetMapping("/updatePage_maemul")
    public String updateMaemul(Model model, Integer maemul_id) {
        System.out.println("maemul_id = " + maemul_id);
        MaemulRegEntity maemulRegEntity = maemulRegService.getMaemulById(maemul_id);
        model.addAttribute("maemulRegEntity", maemulRegEntity);
        return "maemulReg/MaemulUpdate";
    }

}


