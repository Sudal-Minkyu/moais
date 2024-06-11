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
    public List<TodoListDto> findByTodoList(Long adminId, String yyyymm) {

        QTodo todo = QTodo.todo;

        String startDate;
        String endDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        if (yyyymm == null || yyyymm.isEmpty()) {

            LocalDate now = LocalDate.now();
            LocalDate startOfMonth = now.withDayOfMonth(1);
            LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

            startDate = startOfMonth.format(formatter);
            endDate = endOfMonth.format(formatter);

        }
        else {

            DateTimeFormatter yearMonthFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyyMM")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();

            LocalDate month = LocalDate.parse(yyyymm, yearMonthFormatter);
            LocalDate startOfMonth = month.withDayOfMonth(1);
            LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

            startDate = startOfMonth.format(formatter);
            endDate = endOfMonth.format(formatter);

        }

        JPQLQuery<TodoListDto> query = from(todo)
                .where(todo.adminId.eq(adminId).and(todo.tdYyyymmdd.between(startDate, endDate)))
                .orderBy(todo.tdYyyymmdd.asc())
                .select(Projections.constructor(TodoListDto.class,
                        todo.tdYyyymmdd,
                        todo.tdComment,
                        todo.tdState
                ));

        return query.fetch();
    }


}
