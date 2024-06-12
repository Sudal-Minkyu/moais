package com.minkyu.moais.repositorycustom;

import com.minkyu.moais.dto.data.TodoListDto;
import com.minkyu.moais.entity.QTodo;
import com.minkyu.moais.entity.Todo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Repository
public class TodoRepositoryCustomImpl extends QuerydslRepositorySupport implements TodoRepositoryCustom {

    public TodoRepositoryCustomImpl() {
        super(Todo.class);
    }

    @Override
    public List<TodoListDto> findByTodoList(Long adminId) {

        QTodo todo = QTodo.todo;

        JPQLQuery<TodoListDto> query = from(todo)
                .where(todo.adminId.eq(adminId))
                .orderBy(todo.tdYyyymmdd.asc())
                .select(Projections.constructor(TodoListDto.class,
                        todo.tdYyyymmdd,
                        todo.tdComment,
                        todo.tdState
                ));

        return query.fetch();
    }


}
