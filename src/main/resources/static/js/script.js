// script.js

function openModal() {
    $("#myModal").fadeIn().css('display', 'flex');
}

function closeModal() {
    $("#myModal").fadeOut();
}

// 회원가입
function signup() {

    const id = document.getElementById('signup-id').value;
    const password = document.getElementById('signup-password').value;
    const nickname = document.getElementById('signup-nickname').value;

    if (!id) {
        alert('아이디를 입력해주세요.');
        return false;
    }

    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return false;
    }

    if (!nickname) {
        alert('닉네임을 입력해주세요.');
        return false;
    }

    axios.post('/moais/v1/api/signup', {
        userId: id,
        password: password,
        userNickname: nickname
    })

    .then(function (res) {
        $("#signup-id").val("");
        $("#signup-password").val("");
        $("#signup-nickname").val("");

        alert(res.data.response.nickname+'님 회원가입 완료');

        closeModal();
    })

    .catch(function (error) {
        alert('회원가입 실패');
    });

}

// 로그인
function login() {

    const id = document.getElementById('login-id').value;
    const password = document.getElementById('login-password').value;

    if (!id) {
        alert('아이디를 입력해주세요.');
        return false;
    }

    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return false;
    }

    axios.post('/moais/v1/api/login', {
        userId: id,
        password: password
    })

    .then(function (res) {
        $("#login-id").val("");
        $("#login-password").val("");
        $("#signup-nickname").val("");

        if(res.data.status === 500) {
            alert(res.data.err_msg);
        } else {
            console.log(res.data.response.accessToken);

            // 로컬저장소에 넣기
            alert('로그인 완료');
        }
    })

}
