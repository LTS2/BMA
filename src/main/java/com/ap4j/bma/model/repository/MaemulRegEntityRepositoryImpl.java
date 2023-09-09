package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.ap4j.bma.model.entity.meamulReg.QMaemulRegEntity.maemulRegEntity;

@Slf4j
@RequiredArgsConstructor
public class MaemulRegEntityRepositoryImpl implements MaemulRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MaemulRegEntity> findMaemulListBounds(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng, String tradeTypes
            , Integer numberOfRooms, Integer numberOfBathrooms, Integer floorNumber, Integer managementFee, String Elevator, String direction, String Parking
            , String shortTermRental, String keyword) {

        BooleanExpression tradeTypeCondition = null;        // 거래종류
        BooleanExpression roomCountCondition = null;        // 방개수
        BooleanExpression bathroomCountCondition = null;    // 욕실수
        BooleanExpression floorNumberCondition = null;      // 층수
        BooleanExpression managementFeeCondition = null;    // 관리비
        BooleanExpression elevatorCondition = null;         // 엘리베이터
        BooleanExpression directionCondition = null;        // 방향
        BooleanExpression parkingCondition = null;          // 주차가능
        BooleanExpression rentalCondition = null;          // 단기임대
        BooleanExpression keywordCondition = null;          // 키워드

        /* 거래종류 필터 */
        if (!StringUtils.isEmpty(tradeTypes)) {
            // tradeTypes 문자열을 쉼표로 분리하여 배열로 변환
            String[] tradeTypeArray = tradeTypes.split(",");

            // 배열을 사용하여 in 조건 설정
            tradeTypeCondition = maemulRegEntity.tradeType.in(tradeTypeArray);
        }

        /* 방개수 필터 */
        if (numberOfRooms != null) {

            if (numberOfRooms == 4) {
                // "4개 이상"인 경우 gte (Greater Than or Equal) 연산 사용
                roomCountCondition = maemulRegEntity.numberOfRooms.goe(4);
            } else {
                // 다른 경우는 해당 개수와 일치하는 데이터를 찾음
                roomCountCondition = maemulRegEntity.numberOfRooms.eq(numberOfRooms);
            }
        }

        /* 욕실수 필터 */
        if (numberOfBathrooms != null) {

            if (numberOfBathrooms == 4) {
                // "4개 이상"인 경우 gte (Greater Than or Equal) 연산 사용
                bathroomCountCondition = maemulRegEntity.numberOfBathrooms.goe(4);
            } else {
                // 다른 경우는 해당 개수와 일치하는 데이터를 찾음
                bathroomCountCondition = maemulRegEntity.numberOfBathrooms.eq(numberOfBathrooms);
            }
        }

        /* 층수 필터 */
        if (floorNumber != null) {

            if (floorNumber == 4) {
                // "5개 이하"인 경우 loe 연산 사용
                floorNumberCondition = maemulRegEntity.floorNumber.loe(floorNumber);
            } else {
                // 다른 경우에는 goe 연산 사용해서 "해당층 이상" 이런 형태
                floorNumberCondition = maemulRegEntity.floorNumber.goe(floorNumber);
            }
        }

        /* 관리비 필터 */
        if (managementFee != null) {

            switch (managementFee) {
                case 10:
                    managementFeeCondition = maemulRegEntity.managementFee.loe(10);
                    break;
                case 20:
                    managementFeeCondition = maemulRegEntity.managementFee.goe(10).and(maemulRegEntity.managementFee.loe(20));
                    break;
                case 30:
                    managementFeeCondition = maemulRegEntity.managementFee.goe(20).and(maemulRegEntity.managementFee.loe(30));
                    break;
                case 40:
                    managementFeeCondition = maemulRegEntity.managementFee.goe(30).and(maemulRegEntity.managementFee.loe(40));
                    break;
                default:
                    // 기본값 설정 (예: "해당 층 이상")
                    managementFeeCondition = maemulRegEntity.managementFee.goe(managementFee);
                    break;
            }
        }

        /* 엘리베이터 필터 */
        if (Elevator != null) {
            if (Elevator.equals("유")) {
                elevatorCondition = maemulRegEntity.Elevator.eq("유");
            } else if (Elevator.equals("무")) {
                elevatorCondition = maemulRegEntity.Elevator.eq("무");
            }
        }

        /* 방향 필터 */
        if (!StringUtils.isEmpty(direction)) {
            // tradeTypes 문자열을 쉼표로 분리하여 배열로 변환
            String[] directionArray = direction.split(",");

            // 배열을 사용하여 in 조건 설정
            directionCondition = maemulRegEntity.direction.in(directionArray);
        }


        /* 주차가능 필터 */
        if (Parking != null) {
            if (Parking.equals("가능")) {
                parkingCondition = maemulRegEntity.Parking.eq("가능");
            } else if (Parking.equals("불가능")) {
                parkingCondition = maemulRegEntity.Parking.eq("불가능");
            }
        }

        /* 단기임대 필터 */
        if (shortTermRental != null) {
            if (shortTermRental.equals("가능")) {
                rentalCondition = maemulRegEntity.shortTermRental.eq("가능");
            } else if (shortTermRental.equals("불가능")) {
                rentalCondition = maemulRegEntity.shortTermRental.eq("불가능");
            }
        }

        /* 키워드 없을 때*/
        if(!StringUtils.isEmpty(keyword)){
            keywordCondition = maemulRegEntity.APT_name.like("%" + keyword + "%")
                    .or(maemulRegEntity.address.like("%" + keyword + "%"));
        }

        return queryFactory
                .selectFrom(maemulRegEntity)
                .where(
                        maemulRegEntity.latitude.goe(southWestLat),
                        maemulRegEntity.latitude.loe(northEastLat),
                        maemulRegEntity.longitude.goe(southWestLng),
                        maemulRegEntity.longitude.loe(northEastLng),
                        tradeTypeCondition,
                        roomCountCondition,
                        bathroomCountCondition,
                        floorNumberCondition,
                        managementFeeCondition,
                        elevatorCondition,
                        directionCondition,
                        parkingCondition,
                        rentalCondition,
                        keywordCondition
                )
                .fetch();
    }

//    private List<BooleanExpression> eqTradeType(List<String> tradeTypes) {
//        List<BooleanExpression> tradeTypeConditions = new ArrayList<>();
//
//        if (tradeTypes != null && !tradeTypes.isEmpty()) {
//            for (String tradeType : tradeTypes) {
//                tradeTypeConditions.add(maemulRegEntity.tradeType.eq(tradeType));
//            }
//        }
//
//        return tradeTypeConditions;
//    }


}