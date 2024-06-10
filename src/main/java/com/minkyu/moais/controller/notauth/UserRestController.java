package com.minkyu.moais.controller.notauth;

import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.SignUpDto;
import com.minkyu.moais.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "Basic API", description = "로그인 및 회원가입 API")
@RestController
@RequestMapping("/moais/v1/api/")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "회원가입 API", description = "회원가입 정보를 받는다.")
    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signUpUser(@RequestBody @Validated SignUpDto signUpDto) {
        return userService.signUpUser(signUpDto);
    }

    @Operation(summary = "로그인 API", description = "아이디와 비밀번호를 받는다.")
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginUser(@RequestBody @Validated LoginDto loginDto) {
        return userService.loginUser(loginDto);
    }

}