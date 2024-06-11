package com.minkyu.moais.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.beans.ConstructorProperties;

@Data
@Builder
public class SignUpDto {

    @NotBlank(message = "아이디는 필수 입력값 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값 입니다.")
    private String userNickname;

    @ConstructorProperties({"userId", "password", "userNickname"})
    public SignUpDto(String userId, String password, String userNickname) {
        this.userId = userId;
        this.password = password;
        this.userNickname = userNickname;
    }

}