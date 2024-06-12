package com.minkyu.moais.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.minkyu.moais.dto.LoginDto;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InfoRestControllerTest {

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
    @DisplayName("/moais/v2/api/info/secession 성공 테스트")
    void secession1() throws Exception {

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

        ResultActions loginResult = mockMvc.perform(post("/moais/v1/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..response[?(@.accessToken)].accessToken", notNullValue()))
                .andDo(print());

        // 로그인된 jwt 토큰
        String accessToken = JsonPath.read(loginResult.andReturn().getResponse().getContentAsString(), "$.response.accessToken");

        // expected
        mockMvc.perform(post("/moais/v2/api/info/secession")
                        .contentType(APPLICATION_JSON)
                        .header("Authoriztion", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$..response[?(@.result)].result").value("회원탈퇴 완료"))
                .andDo(print());
    }

}



