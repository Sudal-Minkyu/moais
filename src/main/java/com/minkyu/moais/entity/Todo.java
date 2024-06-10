package com.minkyu.moais.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(of = "todoId")
@Table(name="mas_todo")
public class Todo {

    @Id
    @Column(name = "todo_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "td_comment")
    private String tdComment; // 내용

    @Column(name = "td_state")
    private Integer tdState; // 상태 : 1(할일), 2(진행중), 3(완료), 4(대기)

}
