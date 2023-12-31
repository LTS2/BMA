$(document).ready(function() {
     // 비밀번호/비밀번호 확인 일치 여부 체크
    function checkPassword() {
        const pwdValue = $('#pwd').val().trim();
        const pwdCheckValue = $('#pwdCheck').val().trim();
        const pwdreg = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;

        // 비밀번호 일치 여부 체크
        if (pwdValue !== pwdCheckValue) {
            $("#opwdCheck1").css("display", "block");
        } else {
            $("#opwdCheck1").css("display", "none");
        }

        // 비밀번호 형식 체크
        if (pwdValue.length < 8 || pwdValue.length > 20 || !pwdreg.test(pwdValue)) {
            $("#opwdCheck2").css("display", "block");
        } else {
            $("#opwdCheck2").css("display", "none");
        }
    }
    // 비밀번호 일치 여부 체크
    $("#pwd, #pwdCheck").on("input", checkPassword);

    // 비밀번호 형식 체크
    $("#pwd").on("keyup", checkPassword);

    // 연락처 입력 창 형식
    $("#tel").keyup(function(){
        let telValue = $("#tel").val().trim();
        telValue = telValue.replace(/[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]|[^\w\s-]/g, "").replace(/-/g, "");
        $("#tel").val(telValue);
    });

    // 닉네임 입력값 변경 체크
    $("#nickname").on("input", function() {
        $('#btnNicknameCheck').attr('class','btn btn-outline-dark');
        $('#btnNicknameCheck').val("중복검사");
    });
});

// 닉네임 중복검사
let previousNickname = $('#nickname').val().trim();    // 기존 닉네임 저장
function checkNickname() {
    let nickname = $('#nickname').val().trim() ;
    if(nickname === '') {
        alert('닉네임을 입력해주세요.');
        return;
    }
    // 이전 닉네임과 새로운 닉네임을 비교하여 변경 여부 확인
    if (previousNickname !== nickname) {
        $.ajax({
            url: '/member/qNicknameDuplicationCheck',
            type: 'post',
            data: {nickname:nickname},
            success:function(cnt) {
                if(cnt == 0) {
                    $('#btnNicknameCheck').attr('class','btn btn-primary');
                    $('#btnNicknameCheck').val("사용가능");
                    alert('닉네임 사용이 가능합니다.');
                } else {
                    $('#btnNicknameCheck').attr('class','btn btn-danger');
                    $('#btnNicknameCheck').val("사용불가");
                    alert('이미 존재하는 닉네임입니다.\n닉네임을 다시 입력해주세요.');
                //    $('#email').val('');
                }
            },
            error:function() {
                alert("에러입니다.");
            }
        });
    } else {
        alert('변경사항이 없습니다.');
    }
}

// 내정보수정 submit 전에 공백 및 유효성 체크
function oUpdateCheck() {
    const telreg = /^\d{10,11}$/;
    const pwdreg = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;
    let name = $('#name').val().trim();
    let nickname = $('#nickname').val().trim();
    let tel = $('#tel').val().trim();
    let pwdValue = $('#pwd').val();
    let nicknameCheckOk = $("#btnNicknameCheck").val() === '사용가능';

    if(name === '') {
        alert('이름을 입력해주세요.');
        return false;
    }
    if(nickname === '') {
        alert('닉네임을 입력해주세요.');
        return false;
    }
    if(tel === '') {
        alert('연락처를 입력해주세요.');
        return false;
    }
    if (telreg.test(tel) == false) {
        alert('연락처 형식으로 입력해주세요.');
        return false;
    }
    if(pwdValue !== '') {    // 비밀번호창 입력이 있을 경우에만 체크
        if (pwdValue.length < 8 || pwdValue.length > 20) {
            alert('비밀번호는 8자 이상, 20자 이하로 입력해주세요.');
            return false;
        }
        // 비밀번호 형식 체크 - 숫자와 영문 조합
        if (!pwdreg.test(pwdValue)) {
            alert('비밀번호는 숫자와 영문 조합으로 입력해주세요.');
            return false;
        }
        if (!nicknameCheckOk) {
            alert('닉네임 중복 검사를 확인해주세요.');
            return false;
        }
    }
    let confirmUpdate = confirm('입력한 정보로 수정하시겠습니까?');
    if(!confirmUpdate) return false;
}
