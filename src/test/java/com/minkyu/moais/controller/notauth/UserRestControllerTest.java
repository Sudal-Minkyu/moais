package com.minkyu.moais.controller.notauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.SignUpDto;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("/moais/v1/api/singup 성공 테스트")
    void singup1() throws Exception {

        // given
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserId("ab12");
        signUpDto.setPassword("123456");
        signUpDto.setUserNickname("테스트");

        String json = objectMapper.writeValueAsString(signUpDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$..response[?(@.nickname)].nickname").value("테스트"))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v1/api/login 성공 테스트")
    void login1() throws Exception {

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

        String json = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..response[?(@.accessToken)].accessToken", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v1/api/login 실패 테스트1 : 아이디 누락")
    void login2() throws Exception {

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
        loginDto.setUserId("");
        loginDto.setPassword("123456");

        String json = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.err_code").value(400))
                .andExpect(jsonPath("$.message").value("Error"))
                .andExpect(jsonPath("$.err_msg").value("아이디는 필수 입력값 입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v1/api/login 실패 테스트2 : 비밀번호 누락")
    void login3() throws Exception {

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
        loginDto.setPassword("");

        String json = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.err_code").value(400))
                .andExpect(jsonPath("$.message").value("Error"))
                .andExpect(jsonPath("$.err_msg").value("비밀번호는 필수 입력값 입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v1/api/login 실패 테스트3 : 비밀번호 입력 오류")
    void login4() throws Exception {

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
        loginDto.setPassword("1234567");

        String json = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.err_code").value(401))
                .andExpect(jsonPath("$.message").value("Error"))
                .andExpect(jsonPath("$.err_msg").value("아이디 또는 비밀번호가 올바르지 않습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v1/api/login 실패 테스트4 : 탈퇴한 회원 로그인 시")
    void login5() throws Exception {

        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(2)
                .build();
        userRepository.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserId("ab12");
        loginDto.setPassword("123456");

        String json = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.err_code").value(400))
                .andExpect(jsonPath("$.message").value("Error"))
                .andExpect(jsonPath("$.err_msg").value("탈퇴한 회원입니다."))
                .andDo(print());
    }

}



