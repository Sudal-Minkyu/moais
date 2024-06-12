package com.minkyu.moais.service;

import com.minkyu.moais.entity.User;
import com.minkyu.moais.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class InfoServiceTest {

    @Autowired
    private InfoService infoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원탈퇴 성공 테스트")
    void secession1() {

        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(1)
                .build();
        User saveUser = userRepository.save(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("adminId")).thenReturn(saveUser.getAdminId());

        // when
        ResponseEntity<Map<String, Object>> response = infoService.secession(request);

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().get("status"));
        assertEquals("SUCCESS", response.getBody().get("message"));

    }

    @Test
    @DisplayName("회원탈퇴 실패 테스트 : 존재하지 않은 회원입니다.")
    void secession2() {

        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("adminId")).thenReturn(1L);

        // when
        ResponseEntity<Map<String, Object>> response = infoService.secession(request);

        // expected
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().get("err_code"));
        assertEquals("존재하지 않은 회원입니다.", response.getBody().get("err_msg"));
    }

}