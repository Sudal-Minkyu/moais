package com.minkyu.moais.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoDto {

    @NotBlank(message = "내용은 필수 입력값 입니다.")
    private String tdComment; // 내용

}