package com.ap4j.bma.service.member;

import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.ap4j.bma.model.entity.member.LikedEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.repository.LikedRepository;
import com.ap4j.bma.model.repository.MaemulRegEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LikedService {
    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private MaemulRegEntityRepository maemulRegEntityRepository;

    public Long countAll() {
        return likedRepository.count();
    }

    public List<LikedEntity> getAllList() {
        return likedRepository.findAll();
    }

    /** 특정 닉네임이랑 매치되는 Liked 전부 조회 */
    public List<LikedEntity> findLikedByNickname(String nickname) {
        return likedRepository.findLikedByNickname(nickname);
    }

    /** 특정 주소와 매치되는 매물 전부 조회 */
    public List<MaemulRegEntity> findLikedByRoadname(String road_name) {
        return likedRepository.findMaemulByRoadName(road_name);
    }

    /** 로그인한 멤버가 관심등록한 매물만 조회 */
    public List<MaemulRegEntity> filterMaemulListByNickname(String nickname, List<LikedEntity> likedList, List<MaemulRegEntity> maemulList) {
        return likedList.stream()
                .filter(likedEntity -> likedEntity.getNickname().equals(nickname))
                .flatMap(likedEntity -> maemulList.stream()
                        .filter(maemulRegEntity -> likedEntity.getRoad_name().equals(maemulRegEntity.getAddress())))
                .collect(Collectors.toList());
    }


    /** 관심 매물 저장 (*중복 저장 안되게 작업중) */
    @Transactional
    public Long save(LikedEntity likeEntity) {
        String roadName = likeEntity.getRoad_name();
        String nickname = likeEntity.getNickname();
        boolean isDuplicate = likedRepository.existsByNicknameAndRoadName(roadName, nickname);
        log.info("중복여부 : " + isDuplicate);
        if(!isDuplicate) {
            likedRepository.save(likeEntity);
        } else {
            log.info("중복된 매물이네요.");
        }
        return likeEntity.getId();
    }
    
//    public List<MaemulRegEntity> findLikedByRoadname(String roadName) {
//        List<MaemulRegEntity> likedmList = likedRepository.findMaemulByRoadName(roadName);
//
//        return likedRepository.findMaemulByRoadName(maemulList).stream()
//                .map(LikedEntity::getMaemul)
//                .collect(Collectors.toList());
//    }

//    public List<MaemulRegEntity> findMaemulByRoadNames(List<String> roadNames) {
//        List<LikedEntity> likedEntities = likedRepository.findByMaemulRoadNameIn(roadNames);
//
//        // LikedEntity에서 MaemulRegEntity로 변환
//        List<MaemulRegEntity> maemulRegEntities = likedEntities.stream()
//                .map(LikedEntity::getMaemul)
//                .collect(Collectors.toList());
//
//        return maemulRegEntities;
//    }
}
