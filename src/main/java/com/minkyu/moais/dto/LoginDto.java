package com.minkyu.moais.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "아이디는 필수 입력값 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

}