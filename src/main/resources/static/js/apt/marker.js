/** DB에 저장된 좌표를 가지고 사용자가 보고 있는 화면의 좌표값과 비교하여 그 범위에만 마커를 찍어주는 javascript 파일입니다. */
/** 불필요한 데이터를 가져오지 않아서 성능향상에 도움이 됩니다. */

// 카카오 지도 불러오기
var container = document.getElementById('map');
var options = {
    center: new kakao.maps.LatLng(37.535814, 127.008644), // 초기 맵 중심 좌표
    level: 4 // 초기 줌 레벨
};
var map = new kakao.maps.Map(container, options);

// 줌 컨트롤러 지도에 추가
var zoomControl = new kakao.maps.ZoomControl();
map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

// 클러스터
var clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    gridSize: 300,
    averageCenter: false,
    minLevel: 5
});

// 생성된 마커 저장 객체
var existingMarkers = {};

// 마커 생성 함수
function createMarker(position, markerContent, responseData) {
    var markerKey = position.toString();

    var imageSrc = '/img/mapDetailAndAPTList/houseMarker.png',
        imageSize = new kakao.maps.Size(32, 32),
        imageOption = { offset: new kakao.maps.Point(20, 20) };
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

    // 마커가 생성되어 있지 않다면 마커 생성
    if (!existingMarkers[markerKey]) {
        var marker = new kakao.maps.Marker({
            position: position,
            image: markerImage,
            clickable: true
        });

        marker.responseData = responseData; // 첫 화면에서 사이드바 띄워줄 때 사용함

        var overlayContent = markerContent;

        // 오버레이 생성
        var overlay = new kakao.maps.CustomOverlay({
            content: overlayContent,
            map: map,
            position: marker.getPosition(),
            zIndex: 9999
        });

        marker.overlay = overlay;
        marker.overlay.setMap(null); // 오버레이는 초기에 닫혀있는 상태

        // 마커 클릭시 오버레이 열리는 이벤트
        kakao.maps.event.addListener(marker, 'click', function () {
<<<<<<< HEAD
            closeOtherOverlays();
            if (marker.overlay) {
                if (marker.overlay.getMap()) {
                    marker.overlay.setMap(null);
                } else {
                    marker.overlay.setMap(map);
                }
            }

            // 사이드바 미니 지도 (staticMap)
            var staticMapContainer = document.getElementById('staticMap');
                staticMapContainer.innerHTML = ''; // 초기화 코드
            var markerPosition = marker.getPosition();
            var staticMapOption = {
                center: markerPosition,
                level: 4,
                marker: marker
            };
            var staticMap = new kakao.maps.StaticMap(staticMapContainer, staticMapOption);

            // 사이드바 정보 최신화
            var title = responseData.complexName; // 아파트 이름
            var roadName = responseData.roadName; // 도로명 주소
            var address1 = responseData.district // 구주소
            var address2 = responseData.address // 지번

            // 사이드바 상단 타이틀
            var asideBar = document.querySelector('.asideBarAll');
            var titleElement = asideBar.querySelector('.title');

            // 사이드바 내용 부분
            var mapInfo = document.querySelector('.map-info');
            var mapInfoTitleElement = mapInfo.querySelector('.title');
            var mapInfoAddressElement = mapInfo.querySelector('.address');
            var mapInfoRoadNameElement = mapInfo.querySelector('.roadName');

            titleElement.textContent = title;

            mapInfoTitleElement.textContent = title;
            mapInfoAddressElement.textContent = address1 + " " + address2;
            mapInfoRoadNameElement.textContent  = roadName;

            $(".tbl tbody").empty();
                        // 도로명 controller에 전달
                        $.ajax({
                            type: 'POST',
                            url: '/map/main',
                            data: {
                                roadName: roadName
                            },
                            success: function (response) {
                                var tableBody = $(".tbl tbody"); // 테이블의 tbody 요소 선택
                                        if (response && response.aptRealTradeDTOList && response.aptRealTradeDTOList.length > 0) {
                                            // 실거래정보 (연하늘색 부분)
                                            var amount = response.aptRealTradeDTOList[0].transactionAmount.toString();
                                            var amountSliceFirst = amount.slice(0, amount.length - 1);
                                            var amountSliceLast = amount.slice(-1)
                                            var floor = response.aptRealTradeDTOList[0].floor;
                                            var txt = null;
                                            if(amountSliceLast != 0) {
                                                txt = amountSliceFirst + "억 " + amountSliceLast + "000만원(" + floor + "층)";
                                            } else {
                                                txt = amountSliceFirst + "억(" + floor + "층)";
                                            };

                                            var priceArea = document.querySelector(".price-area");
                                            priceArea.querySelector(".txt").textContent = txt;


                                            // 거래이력 테이블
                                            for (var i = 0; i < response.aptRealTradeDTOList.length; i++) {
                                                var detailItem = response.aptRealTradeDTOList[i];

                                                // transactionAmount를 1억과 1억 미만의 금액으로 분리
                                                var amount = detailItem.transactionAmount.toString();
                                                var amountSliceFirst = amount.slice(0, amount.length - 1);
                                                var amountSliceLast = amount.slice(-1)

                                                // 테이블 행 생성 및 데이터 추가
                                                var row = $("<tr>");
                                                $("<td>").text(detailItem.area).appendTo(row);
                                                $("<td>").text(detailItem.floor).appendTo(row);
                                                $("<td>").text(detailItem.contractYearMonth).appendTo(row);
                                               래

                                                // 테이블에 행 추가
                                                tableBody.append(row);
                                            }
                                } else {
                                    console.log("표시할 aptDetailList 데이터가 없습니다.");
                                }
                            }
                        });
                    });


                    clusterer.addMarker(marker); // 클러스터에 마커 추가
                    existingMarkers[markerKey] = marker; // 생성된 마커를 객체에 추가
                }
            }
=======
            closeOtherOverlays(); // 다른 오버레이 닫기
            openOverlay(marker.overlay); // 오버레이 열기
            updateSidebar(responseData); // 사이드바에 데이터 전송
            updateTransactionTable(responseData.roadName); // 사이드바의 거래내역 테이블에 데이터 전송
        });

        clusterer.addMarker(marker); // 클러스터러에 마커 추가
        existingMarkers[markerKey] = marker; // 생성된 마커를 마커리스트에 저장
    }
}
>>>>>>> seoYoung

// 한 번만 실행하기 위한 변수
var onlyOneStart = false;
// 맵 로드가 완료되면 실행
kakao.maps.event.addListener(map, 'tilesloaded', function () {
    // 이미 실행된 경우 함수 종료
    if (onlyOneStart) {
        return;
    }
    onlyOneStart = true; // 변수 업데이트

    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();

    var dataToSend = {
        southWestLat: southWest.getLat(),
        southWestLng: southWest.getLng(),
        northEastLat: northEast.getLat(),
        northEastLng: northEast.getLng(),
    };

    $.ajax({
        type: 'POST',
        url: '/map/main',
        data: dataToSend,
        success: function (response) {
            if (response && response.aptList && response.aptList.length > 0) {
                response.aptList.forEach(function (apt) {
                    var markerPosition = new kakao.maps.LatLng(apt.latitude, apt.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + apt.apartmentName + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + apt.roadName + "</p>" +
                        "</div>" +
                        "</div>";

                    if (!existingMarkers[markerKey]) {
                        createMarker(markerPosition, markerContent, apt);
                    }
                });
            } else {
                console.log("표시할 마커가 없습니다. (좌표값 누락)");
            }
            // 아파트 이름이 "한남더힐"인 마커 찾기
            for (var key in existingMarkers) {
                if (existingMarkers.hasOwnProperty(key)) {
                    var marker = existingMarkers[key];
                    if (marker.overlay && marker.overlay.getContent().includes("한남더힐")) {
                        openOverlay(marker.overlay);
                        updateSidebar(marker.responseData);
                        updateTransactionTable(marker.responseData.roadName);
                        break;
                    }
                }
            }
        }
    });
});

// 맵 이동시 현재 맵의 경계를 기준으로 데이터 요청하고 그 범위에 속하는 데이터의 마커 생성
kakao.maps.event.addListener(map, 'idle', function () {
    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();

    var dataToSend = {
        southWestLat: southWest.getLat(),
        southWestLng: southWest.getLng(),
        northEastLat: northEast.getLat(),
        northEastLng: northEast.getLng(),
    };

    $.ajax({
        type: 'POST',
        url: '/map/main',
        data: dataToSend,
        success: function (response) {
            if (response && response.aptList && response.aptList.length > 0) {
                response.aptList.forEach(function (apt) {
                    var markerPosition = new kakao.maps.LatLng(apt.latitude, apt.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + apt.apartmentName + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + apt.roadName + "</p>" +
                        "</div>" +
                        "</div>";

                    if (!existingMarkers[markerKey]) {
                        createMarker(markerPosition, markerContent, apt);
                    }
                });
            } else {
                console.log("표시할 마커가 없습니다. (좌표값 누락)");
            }
        }
    });
});

// 오버레이 열기
function openOverlay(overlay) {
    if (overlay) {
            overlay.setMap(map);
    }
}

// 사이드바 정보 업데이트
function updateSidebar(responseData) {
    // 사이드바 미니 지도 (staticMap)
    var staticMapContainer = document.getElementById('staticMap');
    staticMapContainer.innerHTML = ''; // 초기화 코드
    var markerPosition = new kakao.maps.LatLng(responseData.latitude, responseData.longitude);
    var staticMapOption = {
        center: markerPosition,
        level: 4,
        marker: new kakao.maps.Marker
    };
    var staticMap = new kakao.maps.StaticMap(staticMapContainer, staticMapOption);

    // 사이드바 타이틀과 상세정보
    var asideBar = document.querySelector('.asideBarAll');
    var titleElement = asideBar.querySelector('.title');

    var mapInfo = document.querySelector('.map-info');
    var mapInfoTitleElement = mapInfo.querySelector('.title');
    var mapInfoAddressElement = mapInfo.querySelector('.address');
    var mapInfoRoadNameElement = mapInfo.querySelector('.roadName');

    var title = responseData.apartmentName; // 타이틀 : 아파트이름
    var roadName = responseData.roadName;
    var address1 = responseData.district;
    var address2 = responseData.address;

    titleElement.textContent = title;

    mapInfoTitleElement.textContent = title;
    mapInfoAddressElement.textContent = address1 + " " + address2;
    mapInfoRoadNameElement.textContent = roadName;
}

function updateTransactionTable(roadName) {
    $.ajax({
        type: 'POST',
        url: '/map/main',
        data: {
            roadName: roadName
        },
        success: function (response) {
            var tableBody = $(".tbl tbody");
            tableBody.empty();
            if (response && response.aptRealTradeDTOList && response.aptRealTradeDTOList.length > 0) {

                // 실거래정보 (연하늘색 부분) - 최근 거래된 금액
                var floor = response.aptRealTradeDTOList[0].floor;
                var amount = response.aptRealTradeDTOList[0].transactionAmount.toString();
                var amountSliceFirst = amount.slice(0, amount.length - 1);
                var amountSliceLast = amount.slice(-1);
                var reformatAmount = null;

                if (amountSliceLast != 0) {
                    reformatAmount = amountSliceFirst + "억 " + amountSliceLast + "000만원(" + floor + "층)";
                } else {
                    reformatAmount = amountSliceFirst + "억(" + floor + "층)";
                };

                var priceArea = document.querySelector(".price-area");
                priceArea.querySelector(".txt").textContent = reformatAmount;

                // 실거래정보 표시
                response.aptRealTradeDTOList.forEach(function (detailItem) {
                    var tableBody = $(".tbl tbody"); // 테이블의 tbody 요소 선택

                    var amount = detailItem.transactionAmount.toString();
                    var amountSliceFirst = amount.slice(0, amount.length - 1);
                    var amountSliceLast = amount.slice(-1)
                    var reformatAmount = null;

                    // 테이블 행 생성 및 데이터 추가
                    var row = $("<tr>");
                    $("<td>").text(detailItem.area).appendTo(row);
                    $("<td>").text(detailItem.floor).appendTo(row);
                    $("<td>").text(detailItem.contractYearMonth).appendTo(row);

                    if (amountSliceLast != 0) {
                        reformatAmount = amountSliceFirst + "억 " + amountSliceLast + "000만원";
                    } else {
                        reformatAmount = amountSliceFirst + "억";
                    }

                    $("<td>").text(reformatAmount).appendTo(row);

                    // 테이블에 행 추가
                    tableBody.append(row);
                });
            } else {
                console.log("표시할 aptDetailList 데이터가 없습니다.");
            }
        }
    });
}

// 다른 오버레이 닫아주는 함수
function closeOtherOverlays() {
    for (var key in existingMarkers) {
        if (existingMarkers.hasOwnProperty(key)) {
            var marker = existingMarkers[key];
            if (marker.overlay) {
                marker.overlay.setMap(null);
            }
        }
    }
}

// keyword 입력 후 Enter 누르면 검색되는 함수
function checkEnter(event) {
<<<<<<< HEAD
    if (event.key === 'Enter') {  // 엔터 키를 눌렀을 때만 작업을 수행
        var keyword = document.querySelector('.aSearchInput').value;  // 입력 필드의 값 가져오기
=======
    if (event.key === 'Enter') {
        // 입력한 키워드 공백 제거
        var keyword = document.querySelector('.aSearchInput').value.replaceAll(' ', '');

        closeOtherOverlays(); // 열려있는 오버레이 닫기
>>>>>>> seoYoung

        $.ajax({
            type: 'POST',
            url: '/map/main',  // 검색을 수행하는 서버의 URL
            data: {
                keyword: keyword
            },
            success: function (response) {
                // 검색 결과에 따라 마커를 생성하고 지도에 표시하기
                if (response && response.aptSearch) {
<<<<<<< HEAD
                var result = response.response.aptSearch;
                console.log(result);
                var markerPosition = new kakao.maps.LatLng(result.latitude, result.longitude);
            }
=======
                    var result = response.aptSearch; // 키워드 검색후 전송받은 해당 아파트 데이터
                    var newCenter = new kakao.maps.LatLng(result.latitude, result.longitude);
                    map.setCenter(newCenter); // 해당 아파트 위치로 센터 변경
                    var markerPosition = new kakao.maps.LatLng(result.latitude, result.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + result.apartmentName + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + result.roadName + "</p>" +
                        "</div>" +
                        "</div>";
                    if (existingMarkers[markerKey]) { // 검색한 아파트가 이미 보고있는 화면에 있어서 마커가 찍혀있으면 기존 마커 사용
                        var marker = existingMarkers[markerKey];
                        openOverlay(marker.overlay);
                        updateSidebar(result);
                        updateTransactionTable(result.roadName);
                    } else { // 새로운 화면으로 이동하여 마커가 없다면 새로운 마커 생성
                        createMarker(markerPosition, markerContent, result);
                    }
                }
>>>>>>> seoYoung
            }
        });
    }
}