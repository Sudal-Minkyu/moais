package com.minkyu.moais.service;

import com.minkyu.moais.common.CommonUtil;
import com.minkyu.moais.common.DataResponse;
import com.minkyu.moais.config.JwtConfig;
import com.minkyu.moais.dto.LoginDto;
import com.minkyu.moais.dto.SignUpDto;
import com.minkyu.moais.entity.Todo;
import com.minkyu.moais.entity.User;
import com.minkyu.moais.handler.exception.CheckSigninException;
import com.minkyu.moais.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class InfoService {

    private final UserRepository userRepository;
    private final DataResponse dataResponse;

    @Autowired
    public InfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.dataResponse = new DataResponse();
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> secession(HttpServletRequest request) {
        log.info("secession 호출");

        HashMap<String, Object> data = new HashMap<>();

        Long adminId = (Long) request.getAttribute("adminId");

        String errmsg;
        Optional<User> optionalUser = userRepository.findById(adminId);
        if(optionalUser.isPresent()) {
            optionalUser.get().setUserState(2);
            userRepository.save(optionalUser.get());
            data.put("result","회원탈퇴 완료");
        } else {
            errmsg = "존재하지 않은 회원입니다.";
            return ResponseEntity.ok(dataResponse.fail("400",errmsg));
        }



        return ResponseEntity.ok(dataResponse.success(data));
    }

}
