package com.minkyu.moais.dto.data;

import lombok.Data;

@Data
public class TodoListDto {

    private String tdYyyymmdd;

    private String tdComment; // 내용

    private Integer tdState; // 상태

    public TodoListDto(String tdYyyymmdd, String tdComment, Integer tdState) {
        this.tdYyyymmdd = tdYyyymmdd;
        this.tdComment = tdComment;
        this.tdState = tdState;
    }

}