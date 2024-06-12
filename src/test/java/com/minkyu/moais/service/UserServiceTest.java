package com.minkyu.moais.service;

import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.SignUpDto;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.handler.exception.CheckSigninException;
import com.minkyu.moais.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpUser1() {

        // given
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserId("ab12");
        signUpDto.setPassword("123456");
        signUpDto.setUserNickname("테스트");

        // when
        userService.signUpUser(signUpDto);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("ab12", user.getUserId());
        assertNotNull(user.getPassword());
        assertTrue(passwordEncoder.matches("123456",user.getPassword()));
        assertEquals("테스트", user.getUserNickname());
        assertEquals(1, user.getUserState());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginUser1() {

        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(1)
                .build();
        userRepository.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserId("ab12");
        loginDto.setPassword("123456");

        // when
        ResponseEntity<Map<String, Object>> response = userService.loginUser(loginDto);

        // then
        assertEquals("SUCCESS", response.getBody().get("message"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 : 아이디 또는 비밀번호가 올바르지 않습니다.")
    void loginUser2() {
        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(1)
                .build();
        userRepository.save(user);

        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setUserId("ab12");
        loginDto.setPassword("1234567");

        // expected
        assertThrows(CheckSigninException.class, () -> userService.loginUser(loginDto));
    }

}