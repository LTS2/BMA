/** 맵 생성 */
var container = document.getElementById('map');
var options = {
    center: new kakao.maps.LatLng(37.55437, 126.974063),
    level: 3
};
var map = new kakao.maps.Map(container, options);

/** 확대축소 컨트롤러 생성 */
var zoomControl = new kakao.maps.ZoomControl();
map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);


var ps = new kakao.maps.services.Places();

for (var i = 0; i < aptList.length; i++) {
    var apt = aptList[i];
    var aptFullAddress = apt.aptAddress + " " + apt.aptName;
    searchKeyword(aptFullAddress, apt.aptName);
}

function searchKeyword(aptFullAddress, aptName) {
    // 키워드로 좌표 변환 요청
    ps.keywordSearch(aptFullAddress, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var latitude = result[0].y; // 위도
            var longitude = result[0].x; // 경도

            // AJAX 요청을 통해 데이터 서버로 전송
            console.log(aptName + " " + latitude + " " + longitude)
            $.ajax({
                url: "/saveCoordinates", // Spring Boot 서버의 URL
                method: "POST", // 전송 방식 (POST)
                data: {
                    latitude: latitude,
                    longitude: longitude,
                    aptName: aptName
                    },
                success: function(response) {
                    // 성공적으로 전송되었을 때 실행되는 코드
                    console.log("Data sent successfully:", response);
                },
                error: function(error) {
                    // 전송 중 에러 발생 시 실행되는 코드
                    console.error("Error sending data:", error);
                }
            });
        }
    });
}







// Places 서비스를 생성
//var places = new kakao.maps.services.Places();
//
//function searchKeywordAndAddMarker(keyword, name, amount) {
//    // 키워드로 장소 검색 요청
//    places.keywordSearch(keyword, function(result, status) {
//        if (status === kakao.maps.services.Status.OK) {
//            var place = result[0];
//            var coords = new kakao.maps.LatLng(place.y, place.x);
//
//            // 마커 생성
//            var marker = new kakao.maps.Marker({
//                map: map,
//                position: coords,
//                title: name
//            });
//
//            // 마커 클릭 시 인포윈도우 생성
//            kakao.maps.event.addListener(marker, 'click', function() {
//                var infowindow = new kakao.maps.InfoWindow({
//                    content: '<div style="padding:10px;">' + name + '</div>'
//                            + '<div style="padding:10px; ">' + keyword + '</div>'
//                            + '<div style="padding:10px;">' + amount + '만원</div>'
//                });
//                infowindow.open(map, marker);
//            });
//        }
//    });
//}
//
//// aptList에 저장된 아파트 정보를 순회하며 키워드 검색 및 마커 생성
//for (var i = 0; i < aptList.length; i++) {
//    var apt = aptList[i];
//
//    // 주소를 세분화하여 키워드로 사용
//    var keyword = apt.aptCityAddress + " " + apt.aptAddress1 + " " + apt.aptAddress2;
//
//    searchKeywordAndAddMarker(keyword, apt.aptName, apt.aptDealAmount);
//
//    console.log(keyword);
//}