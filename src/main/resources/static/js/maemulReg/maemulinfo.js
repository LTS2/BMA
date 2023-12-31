
// HTML 요소
   var monthRadio = document.getElementById("month");
   var yearRadio = document.getElementById("year");
   var sellRadio = document.getElementById("sell");

   var monthRentSection = document.getElementById("monthRentSection");
   var leaseSection = document.getElementById("leaseSection");
   var sellingSection = document.getElementById("sellingSection");

   // 월세 라디오 버튼의 변경 이벤트 리스너
   monthRadio.addEventListener("change", function () {
       if (monthRadio.checked) {
           monthRentSection.style.display = "table"; // 월세 섹션을 표시
           leaseSection.style.display = "none"; // 전세 섹션을 숨김
           sellingSection.style.display = "none"; // 매매 섹션을 숨김

           // 월세 라디오 버튼을 선택한 경우, 전세와 매매 관련 필드의 값을 초기화
           document.getElementById("depositForLease").value = "0";
           document.getElementById("d_managementFee").value = "0";
           document.getElementById("SellingPrice").value = "0";

       }
   });

   // 전세 라디오 버튼의 변경 이벤트 리스너
   yearRadio.addEventListener("change", function () {
       if (yearRadio.checked) {
           monthRentSection.style.display = "none"; // 월세 섹션을 숨김
           leaseSection.style.display = "table"; // 전세 섹션을 표시
           sellingSection.style.display = "none"; // 매매 섹션을 숨김

           // 전세 라디오 버튼을 선택한 경우, 월세와 매매 관련 필드의 값을 초기화

           document.getElementById("monthlyForRent").value = "0";
           document.getElementById("monthlyRent").value = "0";
           document.getElementById("m_managementFee").value = "0";
           document.getElementById("SellingPrice").value = "0";
       }
   });

   // 매매 라디오 버튼의 변경 이벤트 리스너
   sellRadio.addEventListener("change", function () {
       if (sellRadio.checked) {
           monthRentSection.style.display = "none"; // 월세 섹션을 숨김
           leaseSection.style.display = "none"; // 전세 섹션을 숨김
           sellingSection.style.display = "table"; // 매매 섹션을 표시

           // 매매 라디오 버튼을 선택한 경우, 월세와 전세 관련 필드의 값을 초기화
           document.getElementById("monthlyForRent").value = "0";
           document.getElementById("monthlyRent").value = "0";
           document.getElementById("m_managementFee").value = "0";
           document.getElementById("depositForLease").value = "0";
           document.getElementById("d_managementFee").value = "0";
       }
   });

    // 주소 정보 입력란이 모두 채워지면 호출되는 함수
    function showBuildingInfo() {
        // 여기에서 주소 정보 입력란이 모두 채워진지를 확인하고, 그 결과에 따라 건물 정보 입력란을 보이게 합니다.
        var addressInputField = document.getElementById("addressInput");

        if (addressInputField.value !== "") {
            document.getElementById("buildingSection").style.display = "block";
        } else {
            document.getElementById("buildingSection").style.display = "none";
        }
    }

        // 건물 정보 입력란이 모두 채워지면 호출되는 함수
        function showPriceInfo() {
            var buildingInputField = document.getElementById("useInput");

            if (buildingInputField.value !== "") {
                document.getElementById("priceSection").style.display = "block";
            } else {
                document.getElementById("priceSection").style.display = "none";
            }
        }

     // 카카오 도로명 주소 검색 팝업을 열기 위한 함수
       function openKakaoAddressSearch(event) {
           new daum.Postcode({
               oncomplete: function(data) {
                   // 선택한 주소 정보를 입력 필드에 채우는 작업을 수행하세요.
                   document.getElementById('addressInput').value = data.address;
               }
           }).open();
       }


//내위치 기능

//         // 내 위치 정보 가져오기 함수
//            function getCurrentLocation() {
//                if ("geolocation" in navigator) {
//                    navigator.geolocation.getCurrentPosition(function(position) {
//                        // 현재 위치 정보를 처리하는 작업을 수행하세요.
//                        var latitude = position.coords.latitude;
//                        var longitude = position.coords.longitude;
//
//                        // 위도와 경도를 주소로 변환하는 함수 호출
//                        convertLatLongToAddress(latitude, longitude);
//                    });
//                } else {
//                    alert("브라우저가 위치 정보를 지원하지 않습니다.");
//                }
//            }
//
//            // 위도와 경도를 주소로 변환하는 함수
//            function convertLatLongToAddress(latitude, longitude) {
//                var geocoder = new google.maps.Geocoder();
//                var latlng = new google.maps.LatLng(latitude, longitude);
//
//                geocoder.geocode({ 'latLng': latlng }, function (results, status) {
//                    if (status == google.maps.GeocoderStatus.OK) {
//                        if (results[0]) {
//                            var formattedAddress = results[0].formatted_address;
//                            document.getElementById('addressInput').value = formattedAddress;
//                        } else {
//                            alert('주소를 찾을 수 없습니다.');
//                        }
//                    } else {
//                        alert('Geocoder에 실패했습니다: ' + status);
//                    }
//                });
//            }

  //팝업 열기
    function showPopup(event) {
          var popup = document.getElementById("helpPopup");
          popup.style.display = "block";
      }

 // 팝업 닫기
 function closePopup(event) {
     var popup = document.getElementById("helpPopup");
     popup.style.display = "none";

     // 이벤트의 기본 동작(새로고침)을 막음
     event.preventDefault();
 }

  // 숫자를 입력하는 곳에 음수를 입력할 때 경고 메시지를 표시하는 함수
     function checkNonNegativeInput(inputElement, fieldName) {
         const inputValue = parseFloat(inputElement.value);
         if (isNaN(inputValue) || inputValue < 0) {
             alert(fieldName + "에는 음수를 입력할 수 없습니다.");
             inputElement.value = ''; // 입력 값을 비웁니다.
         }
     }

     // 방 수 입력 필드
     const numberOfRoomsInput = document.getElementById('useInput');
     numberOfRoomsInput.addEventListener('blur', function () {
         checkNonNegativeInput(numberOfRoomsInput, '방 수');
     });

     // 욕실 수 입력 필드
     const numberOfBathroomsInput = document.getElementsByName('numberOfBathrooms')[0];
     numberOfBathroomsInput.addEventListener('blur', function () {
         checkNonNegativeInput(numberOfBathroomsInput, '욕실 수');
     });

     // 해당 층 수 입력 필드
     const floorNumberInput = document.getElementsByName('floorNumber')[0];
     floorNumberInput.addEventListener('blur', function () {
         checkNonNegativeInput(floorNumberInput, '해당 층 수');
     });

     // 건물 층 수 입력 필드
     const totalFloorsInput = document.getElementsByName('totalFloors')[0];
     totalFloorsInput.addEventListener('blur', function () {
         checkNonNegativeInput(totalFloorsInput, '건물 층 수');
     });

