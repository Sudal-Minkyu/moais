package com.minkyu.moais.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.TodoDto;
import com.minkyu.moais.entity.Todo;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.repository.TodoRepository;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoRestControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        todoRepository.deleteAll();
    }

    @Test
    @DisplayName("/moais/v2/api/todo/save 성공 테스트")
    void todoSave1() throws Exception {

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

        TodoDto todoDto = new TodoDto();
        todoDto.setTdComment("할 일 내용입니다.");

        String todoJson = objectMapper.writeValueAsString(todoDto);

        // expected
        mockMvc.perform(post("/moais/v2/api/todo/save")
                        .contentType(APPLICATION_JSON)
                        .content(todoJson)
                        .header("Authoriztion", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$..response[?(@.result)].result").value("TODO 등록 완료"))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v2/api/todo/list 성공 테스트")
    void todoList() throws Exception {

        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(1)
                .build();
        User saveUser = userRepository.save(user);

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

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        List<Todo> todoList = new ArrayList<>();
        Todo todo1 = new Todo();
        todo1.setAdminId(saveUser.getAdminId());
        todo1.setTdYyyymmdd(formattedDate);
        todo1.setTdComment("할 일 내용입니다.");
        todo1.setTdState(1);
        todoList.add(todo1);

        Todo todo2 = new Todo();
        todo2.setAdminId(saveUser.getAdminId());
        todo2.setTdYyyymmdd(formattedDate);
        todo2.setTdComment("할 일 내용입니다.2");
        todo2.setTdState(1);
        todoList.add(todo2);

        todoRepository.saveAll(todoList);

        // expected
        mockMvc.perform(get("/moais/v2/api/todo/list")
                        .contentType(APPLICATION_JSON)
                        .header("Authoriztion", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$..response[?(@.result)].result.size()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("/moais/v2/api/todo/update 성공 테스트")
    void todoUpdate() throws Exception {

        String ecnryptedPassword = passwordEncoder.encode("123456");

        // given
        User user = User.builder()
                .userId("ab12")
                .password(ecnryptedPassword)
                .userNickname("테스트")
                .userState(1)
                .build();

        User saveUser = userRepository.save(user);

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

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Todo todo = new Todo();
        todo.setAdminId(saveUser.getAdminId());
        todo.setTdYyyymmdd(formattedDate);
        todo.setTdComment("할 일 내용입니다.");
        todo.setTdState(1);

        Todo saveTodo = todoRepository.save(todo);

        // expected
        mockMvc.perform(post("/moais/v2/api/todo/update")
                        .contentType(APPLICATION_JSON)
                        .param("todoId", String.valueOf(saveTodo.getTodoId()))
                        .param("tdState", String.valueOf(2))
                        .header("Authoriztion", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$..response[?(@.result)].result").value("TODO 수정 완료"))
                .andDo(print());
    }

}



