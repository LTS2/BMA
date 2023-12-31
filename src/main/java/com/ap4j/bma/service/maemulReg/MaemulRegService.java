package com.ap4j.bma.service.maemulReg;

import com.ap4j.bma.model.entity.meamulReg.MaeMulRegDTO;
import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.ap4j.bma.model.repository.MaemulRegEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MaemulRegService {

    private final MaemulRegEntityRepository maemulRegEntityRepository;

    @Autowired
    public MaemulRegService(MaemulRegEntityRepository maemulRegEntityRepository) {
        this.maemulRegEntityRepository = maemulRegEntityRepository;
    }

    // 매물 정보 저장
    @Transactional
    public MaemulRegEntity saveMaemulInfo(MaemulRegEntity maemulRegEntity) {
        return maemulRegEntityRepository.save(maemulRegEntity);
    }

    public MaemulRegEntity getMaemulById(Integer maemulId) {
        // 매물 ID로 매물 정보를 조회
        return maemulRegEntityRepository.findById(maemulId).orElse(null);
    }


    // 매물 좌표값 업데이트
    public void updateMeamulReg(Integer maemulId, Double latitude, Double longitude) {
        MaemulRegEntity updateMaemul = maemulRegEntityRepository.findById(maemulId).orElse(null);
        if (updateMaemul != null) {
            updateMaemul.setLatitude(latitude);
            updateMaemul.setLongitude(longitude);
            maemulRegEntityRepository.save(updateMaemul);
        }
    }

    // 좌표값에 따른 매물 리스트 (마커 찍기용)
    public List<MaeMulRegDTO> findMaemulListBounds(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng, String tradeType
            , Integer roomNumber, Integer numberBathrooms, Integer floorNumber, Integer managementFee, String Elevator, String direction, String Parking
            , String shortTermRental, String keyword, Integer rowSellingPrice, Integer highSellingPrice, Integer rowDepositForLease, Integer highDepositForLease
            , Integer rowMonthlyForRent, Integer highMonthlyForRent,  Double minPrivateArea, Double maxPrivateArea, String orderType) {
        List<MaeMulRegDTO> maeMulRegDTOList = new ArrayList<>();
        List<MaemulRegEntity> maemulRegEntityList = maemulRegEntityRepository.findMaemulListBounds(southWestLat, southWestLng, northEastLat, northEastLng, tradeType
                , roomNumber, numberBathrooms, floorNumber, managementFee, Elevator, direction, Parking, shortTermRental, keyword, rowSellingPrice, highSellingPrice
                , rowDepositForLease, highDepositForLease, rowMonthlyForRent, highMonthlyForRent, minPrivateArea, maxPrivateArea, orderType);

        for (MaemulRegEntity maemulRegEntity : maemulRegEntityList) {
            MaeMulRegDTO maeMulRegDTO = MaeMulRegDTO.builder()
                    .id(maemulRegEntity.getId())
                    .nickname(maemulRegEntity.getNickname())
                    .address(maemulRegEntity.getAddress())
                    .APT_name(maemulRegEntity.getAPT_name())
                    .buildingUsage(maemulRegEntity.getBuildingUsage())
                    .numberOfRooms(maemulRegEntity.getNumberOfRooms())
                    .numberOfBathrooms(maemulRegEntity.getNumberOfBathrooms())
                    .floorNumber(maemulRegEntity.getFloorNumber())
                    .totalFloors(maemulRegEntity.getTotalFloors())
                    .privateArea(maemulRegEntity.getPrivateArea())
                    .supplyArea(maemulRegEntity.getSupplyArea())
                    .direction(maemulRegEntity.getDirection())
                    .heatingType(maemulRegEntity.getHeatingType())
                    .Elevator(maemulRegEntity.getElevator())
                    .Parking(maemulRegEntity.getParking())
                    .totalParking(maemulRegEntity.getTotalParking())
                    .shortTermRental(maemulRegEntity.getShortTermRental())
                    .availableMoveInDate(maemulRegEntity.getAvailableMoveInDate())
                    .loanAmount(maemulRegEntity.getLoanAmount())
                    .tradeType(maemulRegEntity.getTradeType())
                    .monthlyForRent(maemulRegEntity.getMonthlyForRent())
                    .monthlyRent(maemulRegEntity.getMonthlyRent())
                    .depositForLease(maemulRegEntity.getDepositForLease())
                    .managementFee(maemulRegEntity.getManagementFee())
                    .SellingPrice(maemulRegEntity.getSellingPrice())
                    .title(maemulRegEntity.getTitle())
                    .content(maemulRegEntity.getContent())
                    .features(maemulRegEntity.getFeatures())
                    .optional(maemulRegEntity.getOptional())
                    .security(maemulRegEntity.getSecurity())
                    .longitude(maemulRegEntity.getLongitude())
                    .latitude(maemulRegEntity.getLatitude())
                    .build();
            maeMulRegDTOList.add(maeMulRegDTO);
        }
        return maeMulRegDTOList;
    }

    // 마커 클릭시 해당 주소값과 같은 매물 리스트 불러오기
    public List<MaeMulRegDTO> findMaemulByAddress(String address) {
        System.out.println("서비스 address" + address);
        List<MaeMulRegDTO> maeMulRegDTOList = new ArrayList<>();
        List<MaemulRegEntity> maemulRegEntityList = maemulRegEntityRepository.findMaemulByAddress(address);
        for (MaemulRegEntity maemulRegEntity : maemulRegEntityList) {
            MaeMulRegDTO maeMulRegDTO = MaeMulRegDTO.builder()
                    .id(maemulRegEntity.getId())
                    .nickname(maemulRegEntity.getNickname())
                    .address(maemulRegEntity.getAddress())
                    .APT_name(maemulRegEntity.getAPT_name())
                    .buildingUsage(maemulRegEntity.getBuildingUsage())
                    .numberOfRooms(maemulRegEntity.getNumberOfRooms())
                    .numberOfBathrooms(maemulRegEntity.getNumberOfBathrooms())
                    .floorNumber(maemulRegEntity.getFloorNumber())
                    .totalFloors(maemulRegEntity.getTotalFloors())
                    .privateArea(maemulRegEntity.getPrivateArea())
                    .supplyArea(maemulRegEntity.getSupplyArea())
                    .direction(maemulRegEntity.getDirection())
                    .heatingType(maemulRegEntity.getHeatingType())
                    .Elevator(maemulRegEntity.getElevator())
                    .Parking(maemulRegEntity.getParking())
                    .totalParking(maemulRegEntity.getTotalParking())
                    .shortTermRental(maemulRegEntity.getShortTermRental())
                    .availableMoveInDate(maemulRegEntity.getAvailableMoveInDate())
                    .loanAmount(maemulRegEntity.getLoanAmount())
                    .tradeType(maemulRegEntity.getTradeType())
                    .monthlyForRent(maemulRegEntity.getMonthlyForRent())
                    .monthlyRent(maemulRegEntity.getMonthlyRent())
                    .depositForLease(maemulRegEntity.getDepositForLease())
                    .managementFee(maemulRegEntity.getManagementFee())
                    .SellingPrice(maemulRegEntity.getSellingPrice())
                    .title(maemulRegEntity.getTitle())
                    .content(maemulRegEntity.getContent())
                    .features(maemulRegEntity.getFeatures())
                    .optional(maemulRegEntity.getOptional())
                    .security(maemulRegEntity.getSecurity())
                    .longitude(maemulRegEntity.getLongitude())
                    .latitude(maemulRegEntity.getLatitude())
                    .createdAt(maemulRegEntity.getCreatedAt())
                    .build();
            maeMulRegDTOList.add(maeMulRegDTO);
        }
        return maeMulRegDTOList;
    }

    // 키워드 검색시 해당 주소 or 아파트 불러오기
    public List<MaeMulRegDTO> findByMaemulKeyword (String keyword) {
        List<MaeMulRegDTO> maeMulRegDTOList = new ArrayList<>();
        List<MaemulRegEntity> maemulRegEntityList = maemulRegEntityRepository.findByMaemulKeyword(keyword);
        for (MaemulRegEntity maemulRegEntity : maemulRegEntityList) {
            MaeMulRegDTO maeMulRegDTO = MaeMulRegDTO.builder()
                    .id(maemulRegEntity.getId())
                    .nickname(maemulRegEntity.getNickname())
                    .address(maemulRegEntity.getAddress())
                    .APT_name(maemulRegEntity.getAPT_name())
                    .buildingUsage(maemulRegEntity.getBuildingUsage())
                    .numberOfRooms(maemulRegEntity.getNumberOfRooms())
                    .numberOfBathrooms(maemulRegEntity.getNumberOfBathrooms())
                    .floorNumber(maemulRegEntity.getFloorNumber())
                    .totalFloors(maemulRegEntity.getTotalFloors())
                    .privateArea(maemulRegEntity.getPrivateArea())
                    .supplyArea(maemulRegEntity.getSupplyArea())
                    .direction(maemulRegEntity.getDirection())
                    .heatingType(maemulRegEntity.getHeatingType())
                    .Elevator(maemulRegEntity.getElevator())
                    .Parking(maemulRegEntity.getParking())
                    .totalParking(maemulRegEntity.getTotalParking())
                    .shortTermRental(maemulRegEntity.getShortTermRental())
                    .availableMoveInDate(maemulRegEntity.getAvailableMoveInDate())
                    .loanAmount(maemulRegEntity.getLoanAmount())
                    .tradeType(maemulRegEntity.getTradeType())
                    .monthlyForRent(maemulRegEntity.getMonthlyForRent())
                    .monthlyRent(maemulRegEntity.getMonthlyRent())
                    .depositForLease(maemulRegEntity.getDepositForLease())
                    .managementFee(maemulRegEntity.getManagementFee())
                    .SellingPrice(maemulRegEntity.getSellingPrice())
                    .title(maemulRegEntity.getTitle())
                    .content(maemulRegEntity.getContent())
                    .features(maemulRegEntity.getFeatures())
                    .optional(maemulRegEntity.getOptional())
                    .security(maemulRegEntity.getSecurity())
                    .longitude(maemulRegEntity.getLongitude())
                    .latitude(maemulRegEntity.getLatitude())
                    .createdAt(maemulRegEntity.getCreatedAt())
                    .build();
            maeMulRegDTOList.add(maeMulRegDTO);
        }
        return maeMulRegDTOList;
    }


    // 주거용 산업용 버튼 클릭시 리스트 출력
    public List<MaeMulRegDTO> findByMaemulButton (String value) {
        List<MaeMulRegDTO> maeMulRegDTOList = new ArrayList<>();
        List<MaemulRegEntity> maemulRegEntityList = maemulRegEntityRepository.findByMaemulButton(value);
        for (MaemulRegEntity maemulRegEntity : maemulRegEntityList) {
            MaeMulRegDTO maeMulRegDTO = MaeMulRegDTO.builder()
                    .id(maemulRegEntity.getId())
                    .nickname(maemulRegEntity.getNickname())
                    .address(maemulRegEntity.getAddress())
                    .APT_name(maemulRegEntity.getAPT_name())
                    .buildingUsage(maemulRegEntity.getBuildingUsage())
                    .numberOfRooms(maemulRegEntity.getNumberOfRooms())
                    .numberOfBathrooms(maemulRegEntity.getNumberOfBathrooms())
                    .floorNumber(maemulRegEntity.getFloorNumber())
                    .totalFloors(maemulRegEntity.getTotalFloors())
                    .privateArea(maemulRegEntity.getPrivateArea())
                    .supplyArea(maemulRegEntity.getSupplyArea())
                    .direction(maemulRegEntity.getDirection())
                    .heatingType(maemulRegEntity.getHeatingType())
                    .Elevator(maemulRegEntity.getElevator())
                    .Parking(maemulRegEntity.getParking())
                    .totalParking(maemulRegEntity.getTotalParking())
                    .shortTermRental(maemulRegEntity.getShortTermRental())
                    .availableMoveInDate(maemulRegEntity.getAvailableMoveInDate())
                    .loanAmount(maemulRegEntity.getLoanAmount())
                    .tradeType(maemulRegEntity.getTradeType())
                    .monthlyForRent(maemulRegEntity.getMonthlyForRent())
                    .monthlyRent(maemulRegEntity.getMonthlyRent())
                    .depositForLease(maemulRegEntity.getDepositForLease())
                    .managementFee(maemulRegEntity.getManagementFee())
                    .SellingPrice(maemulRegEntity.getSellingPrice())
                    .title(maemulRegEntity.getTitle())
                    .content(maemulRegEntity.getContent())
                    .features(maemulRegEntity.getFeatures())
                    .optional(maemulRegEntity.getOptional())
                    .security(maemulRegEntity.getSecurity())
                    .longitude(maemulRegEntity.getLongitude())
                    .latitude(maemulRegEntity.getLatitude())
                    .createdAt(maemulRegEntity.getCreatedAt())
                    .build();
            maeMulRegDTOList.add(maeMulRegDTO);
        }
        return maeMulRegDTOList;
    }

    // 매물 수정
    @Transactional
    public MaemulRegEntity updateMaemul(MaemulRegEntity updatedMaemul) {
        // 업데이트할 매물 엔티티를 찾아옵니다.
        MaemulRegEntity existingMaemul = maemulRegEntityRepository.findMaemulById(updatedMaemul.getId()).orElse(null);

        if (existingMaemul != null) {
            existingMaemul.setAddress(updatedMaemul.getAddress());
            existingMaemul.setAPT_name(updatedMaemul.getAPT_name());
            existingMaemul.setBuildingUsage(updatedMaemul.getBuildingUsage());
            existingMaemul.setNumberOfRooms(updatedMaemul.getNumberOfRooms());
            existingMaemul.setNumberOfBathrooms(updatedMaemul.getNumberOfBathrooms());
            existingMaemul.setFloorNumber(updatedMaemul.getFloorNumber());
            existingMaemul.setTotalFloors(updatedMaemul.getTotalFloors());
            existingMaemul.setPrivateArea(updatedMaemul.getPrivateArea());
            existingMaemul.setSupplyArea(updatedMaemul.getSupplyArea());
            existingMaemul.setDirection(updatedMaemul.getDirection());
            existingMaemul.setHeatingType(updatedMaemul.getHeatingType());
            existingMaemul.setElevator(updatedMaemul.getElevator());
            existingMaemul.setParking(updatedMaemul.getParking());
            existingMaemul.setTotalParking(updatedMaemul.getTotalParking());
            existingMaemul.setShortTermRental(updatedMaemul.getShortTermRental());
            existingMaemul.setAvailableMoveInDate(updatedMaemul.getAvailableMoveInDate());
            existingMaemul.setLoanAmount(updatedMaemul.getLoanAmount());
            existingMaemul.setTradeType(updatedMaemul.getTradeType());
            existingMaemul.setMonthlyForRent(updatedMaemul.getMonthlyForRent());
            existingMaemul.setMonthlyRent(updatedMaemul.getMonthlyRent());
            existingMaemul.setDepositForLease(updatedMaemul.getDepositForLease());
            existingMaemul.setManagementFee(updatedMaemul.getManagementFee());
            existingMaemul.setSellingPrice(updatedMaemul.getSellingPrice());
            existingMaemul.setTitle(updatedMaemul.getTitle());
            existingMaemul.setContent(updatedMaemul.getContent());
            existingMaemul.setFeatures(updatedMaemul.getFeatures());
            existingMaemul.setOptional(updatedMaemul.getOptional());
            existingMaemul.setSecurity(updatedMaemul.getSecurity());
            existingMaemul.setLongitude(updatedMaemul.getLongitude());
            existingMaemul.setLatitude(updatedMaemul.getLatitude());

            return maemulRegEntityRepository.save(existingMaemul);
        } else {
            return null;
        }
    }

    /**조회수 증가 로직 DetailController details()에서 씀*/
    @Transactional
    public void incrementViewCount(int id){
        MaemulRegEntity maemul = maemulRegEntityRepository.findMaemulById(id).orElse(null);
        if (maemul != null) {
            // 현재의 viewCount 값을 가져와 1 증가시킵니다.
            int currentViewCount = maemul.getViewCount();
            maemul.setViewCount(currentViewCount + 1);

            // 변경된 값을 저장합니다.
            maemulRegEntityRepository.save(maemul);
        }
    }

    public Page<MaemulRegEntity> getPageByNickname(String nickname, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<MaemulRegEntity> mmpList = maemulRegEntityRepository.findMaemulByMemberNicknameMy(nickname,pageable);
        return mmpList;
    }


}