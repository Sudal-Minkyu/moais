package com.minkyu.moais.service;

import com.minkyu.moais.common.DataResponse;
import com.minkyu.moais.config.JwtConfig;
import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.SignUpDto;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.handler.exception.CheckSigninException;
import com.minkyu.moais.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final DataResponse dataResponse;

    private final JwtConfig jwtConfig;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.dataResponse = new DataResponse();
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> signUpUser(SignUpDto signUpDto) {
        log.info("signUpUser 호출");

        HashMap<String, Object> data = new HashMap<>();

        User user = User.builder()
                .userId(signUpDto.getUserId())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .userNickname(signUpDto.getUserNickname())
                .userState(1)
                .build();

        userRepository.save(user);

        data.put("user", user.getUserId());

        log.info("'"+user.getUserNickname()+"' 고객님 회원가입 완료");

        return ResponseEntity.ok(dataResponse.success(data));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> loginUser(LoginDto loginDto) {
        log.info("loginUser 호출");

        HashMap<String, Object> data = new HashMap<>();

        User user = userRepository.findByUserId(loginDto.getUserId())
                .orElseThrow(CheckSigninException::new);

        if(user.getUserState() == 2) {
            return ResponseEntity.ok(dataResponse.fail("400","탈퇴한 회원입니다."));
        }

        var matches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        if (!matches) {
            throw new CheckSigninException();
        }

        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecretKey());

        Instant now = Instant.now();
        String jws = Jwts.builder()
                .subject(String.valueOf(user.getAdminId()))
                .signWith(key)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(10, ChronoUnit.MINUTES)))
                .compact();

//        log.info("엑세스 토큰 : "+jws);
//        log.info("로그인 아이디 : "+loginDto.getUserId());
//        log.info("로그인 비밀번호 : "+loginDto.getPassword());

        data.put("accessToken", jws);
        data.put("text", "토큰 유효기간은 10분입니다.");

        return ResponseEntity.ok(dataResponse.success(data));
    }

}
