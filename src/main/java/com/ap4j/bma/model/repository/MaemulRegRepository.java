package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaemulRegRepository extends JpaRepository<MaemulRegEntity, Integer> {

    /** 현재 보고있는 화면의 좌표값에 해당하는 매물 리스트 불러오기 */
    @Query("SELECT m FROM MaemulRegEntity m WHERE m.latitude >= ?1 AND m.latitude <= ?3 AND m.longitude >= ?2 AND m.longitude <= ?4")
    List<MaemulRegEntity> findMaemulListBounds(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng);
}