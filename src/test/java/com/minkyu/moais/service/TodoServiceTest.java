package com.minkyu.moais.service;

import com.minkyu.moais.dto.TodoDto;
import com.minkyu.moais.entity.Todo;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.repository.TodoRepository;
import com.minkyu.moais.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        todoRepository.deleteAll();
    }

    @Test
    @DisplayName("리스트호출 성공 테스트")
    void todoList1() {

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
        ResponseEntity<Map<String, Object>> response = todoService.todoList(request);

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().get("status"));
        assertEquals("SUCCESS", response.getBody().get("message"));
    }

    @Test
    @DisplayName("할일 저장 성공 테스트")
    void todoSave1() {

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

        TodoDto todoDto = new TodoDto();
        todoDto.setTdComment("내용입니다.");

        // when
        ResponseEntity<Map<String, Object>> response = todoService.todoSave(todoDto, request);

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().get("status"));
        assertEquals("SUCCESS", response.getBody().get("message"));
    }

    @Test
    @DisplayName("할일 저장 실패 테스트 : 존재하지 않은 회원입니다.")
    void todoSave2() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("adminId")).thenReturn(1L);

        TodoDto todoDto = new TodoDto();
        todoDto.setTdComment("내용입니다.");

        // when
        ResponseEntity<Map<String, Object>> response = todoService.todoSave(todoDto, request);

        // then
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().get("err_code"));
        assertEquals("존재하지 않은 회원입니다.", response.getBody().get("err_msg"));
    }

    @Test
    @DisplayName("할일 수정 성공 테스트")
    void todoUpdate1() {

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

        List<Todo> saveTodoList = todoRepository.saveAll(todoList);

        // when
        ResponseEntity<Map<String, Object>> response = todoService.todoUpdate(saveTodoList.get(0).getTodoId(), 2, request);

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().get("status"));
        assertEquals("SUCCESS", response.getBody().get("message"));
    }

    @Test
    @DisplayName("할일 수정 실패 테스트 : 진행중인 상태에서만 대기상태를 할 수 있습니다.")
    void todoUpdate2() {

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

        List<Todo> saveTodoList = todoRepository.saveAll(todoList);

        // when
        ResponseEntity<Map<String, Object>> response = todoService.todoUpdate(saveTodoList.get(0).getTodoId(), 4, request);

        // then
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().get("err_code"));
        assertEquals("진행중인 상태에서만 대기상태를 할 수 있습니다.", response.getBody().get("err_msg"));
    }

    @Test
    @DisplayName("할일 수정 실패 테스트 : 해당 게시물을 등록한 회원이 아닙니다.")
    void todoUpdate3() {

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
        when(request.getAttribute("adminId")).thenReturn(2L);

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

        List<Todo> saveTodoList = todoRepository.saveAll(todoList);

        // when
        ResponseEntity<Map<String, Object>> response = todoService.todoUpdate(saveTodoList.get(0).getTodoId(), 2, request);

        // then
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().get("err_code"));
        assertEquals("해당 게시물을 등록한 회원이 아닙니다.", response.getBody().get("err_msg"));
    }

    @Test
    @DisplayName("할일 수정 실패 테스트 : 존재하지 않은 TODO 입니다.")
    void todoUpdate4() {

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
        ResponseEntity<Map<String, Object>> response = todoService.todoUpdate(1L, 1, request);

        // then
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().get("err_code"));
        assertEquals("존재하지 않은 TODO 입니다.", response.getBody().get("err_msg"));
    }

}