package com.minkyu.moais.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "아이디는 필수 입력값 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값 입니다.")
    private String userNickname;

}