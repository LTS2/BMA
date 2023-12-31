package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.community.CommunityEntity;
import com.ap4j.bma.model.entity.customerCenter.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <NoticeEntity, Integer> {

    NoticeEntity findTopByIdLessThanOrderByIdDesc(Integer id);

    NoticeEntity findTopByIdGreaterThanOrderByIdAsc(Integer id);
}
