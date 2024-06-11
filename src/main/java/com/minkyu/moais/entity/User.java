package com.minkyu.moais.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="mas_user")
public class User {

    @Id
    @Column(name = "admin_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_nickname", unique = true)
    private String userNickname;

    @Column(name = "user_state")
    private Integer userState; // 회원상태(정상:1, 탈퇴:2) -> 기본값 1

    @Builder
    public User(String userId, String password, String userNickname, Integer userState) {
        this.userId = userId;
        this.password = password;
        this.userNickname = userNickname;
        this.userState = userState;
    }

}