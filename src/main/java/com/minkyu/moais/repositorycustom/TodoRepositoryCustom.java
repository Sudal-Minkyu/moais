package com.minkyu.moais.repositorycustom;

import com.minkyu.moais.dto.data.TodoListDto;

import java.util.List;

public interface TodoRepositoryCustom {

    List<TodoListDto> findByTodoList(Long adminId, String yyyymm);

}