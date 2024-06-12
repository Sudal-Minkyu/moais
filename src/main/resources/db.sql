CREATE TABLE `mas_user` (
    `admin_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(50) NOT NULL COMMENT '로그인 아이디',
    `password` VARCHAR(255) NOT NULL COMMENT '로그인 비밀번호(암호화)',
    `user_nickname` VARCHAR(50) NOT NULL COMMENT '닉네임',
    `user_state` INT(11) NOT NULL DEFAULT '1' COMMENT '회원상태(정상:1, 탈퇴:2)',
    PRIMARY KEY (`admin_id`),
    UNIQUE (`user_id`)
);

CREATE TABLE `mas_todo` (
    `todo_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `admin_id` BIGINT(20) NOT NULL,
    `td_yyyymmdd` VARCHAR(8) NOT NULL COMMENT '등록한 날짜',
    `td_comment` VARCHAR(255) NOT NULL COMMENT '내용',
    `td_state` INT(1) NOT NULL DEFAULT '1' COMMENT '상태 : 1(TODO), 2(IN PROGRESS), 3(DONE), 4(PENDING)',
    PRIMARY KEY (`todo_id`),
);

CREATE INDEX idx_admin_id ON `mas_todo` (`admin_id`);