package com.minkyu.moais.service;

import com.minkyu.moais.common.CommonUtil;
import com.minkyu.moais.common.DataResponse;
import com.minkyu.moais.dto.TodoDto;
import com.minkyu.moais.dto.data.TodoListDto;
import com.minkyu.moais.entity.Todo;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.repository.TodoRepository;
import com.minkyu.moais.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    private final DataResponse dataResponse;
    private final HashMap<String, Object> data = new HashMap<>();

    @Autowired
    public TodoService(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.dataResponse = new DataResponse();
    }

    public ResponseEntity<Map<String, Object>> todoList(HttpServletRequest request) {
        log.info("todoList 호출");

        HashMap<String, Object> data = new HashMap<>();

        Long adminId = (Long) request.getAttribute("adminId");

        List<TodoListDto> todoListDtos = todoRepository.findByTodoList(adminId);

        if(todoListDtos.size() == 0) {
            data.put("result", "데이터가 존재하지 않습니다.");
        } else {
            data.put("result", todoListDtos);
        }

        return ResponseEntity.ok(dataResponse.success(data));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> todoSave(TodoDto todoDto, HttpServletRequest request) {
        log.info("todoSave 호출");

        Long adminId = (Long) request.getAttribute("adminId");

        String errmsg;
        Optional<User> optionalUser = userRepository.findById(adminId);
        if(optionalUser.isPresent()) {

            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);

            Todo todo = new Todo();
            todo.setAdminId(optionalUser.get().getAdminId());
            todo.setTdYyyymmdd(formattedDate);
            todo.setTdComment(todoDto.getTdComment());
            todo.setTdState(1);

            Todo saveTOdo = todoRepository.save(todo);

            log.info("TODO 등록 완료 : "+saveTOdo.getTodoId());

            data.put("result","TODO 등록 완료");
        } else {
            errmsg = "존재하지 않은 회원입니다.";
            return ResponseEntity.ok(dataResponse.fail("400",errmsg));
        }

        return ResponseEntity.ok(dataResponse.success(data));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> todoUpdate(Long todoId, Integer tdState, HttpServletRequest request) {
        log.info("todoUpdate 호출");

        log.info("todoId : "+todoId);

        Long adminId = (Long) request.getAttribute("adminId");

        String errmsg;
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if(optionalTodo.isPresent()) {
            if(optionalTodo.get().getAdminId().equals(adminId)) {
                optionalTodo.get().setTdState(tdState);
                Todo saveTOdo = todoRepository.save(optionalTodo.get());

                log.info("TODO 수정 완료 : "+saveTOdo.getTodoId());

                data.put("result","TODO 수정 완료");
            } else {
                errmsg = "해당 게시물을 등록한 회원이 아닙니다.";
                return ResponseEntity.ok(dataResponse.fail("400",errmsg));
            }
        } else {
            errmsg = "존재하지 않은 TODO 입니다.";
            return ResponseEntity.ok(dataResponse.fail("400",errmsg));
        }

        return ResponseEntity.ok(dataResponse.success(data));
    }

}
