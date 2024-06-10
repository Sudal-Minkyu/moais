package com.minkyu.moais.controller.auth;

import com.minkyu.moais.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "Todo API", description = "Todo 조회, 추가, 수정 API")
@RestController
@RequestMapping("/moais/v2/api/todo")
public class TodoRestController {

    private final TodoService todoService;

    @Autowired
    public TodoRestController(TodoService todoService){
        this.todoService = todoService;
    }

    @Operation(summary = "Todo 조회 API", description = "등록한 TodoList를 조회한다.")
    @GetMapping("/list")
    public ResponseEntity<Map<String,Object>> list(HttpServletRequest request) {
        return todoService.list(request);
    }

    @Operation(summary = "Todo 추가 API", description = "등록한 Todo를 추가한다.")
    @PostMapping("/save")
    public ResponseEntity<Map<String,Object>> save(HttpServletRequest request) {
        return todoService.save(request);
    }

    @Operation(summary = "Todo 수정 API", description = "등록한 Todo를 수정한다.")
    @PostMapping("/update")
    public ResponseEntity<Map<String,Object>> update(HttpServletRequest request) {
        return todoService.update(request);
    }

}