package com.minkyu.moais.handler;

import com.minkyu.moais.config.JwtConfig;
import com.minkyu.moais.handler.exception.CheckAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Slf4j
public class CustomInterceptor implements AsyncHandlerInterceptor {

    private final JwtConfig jwtConfig;

    public CustomInterceptor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader("Authoriztion");
//        log.info("token: " + token);

        if (token == null || token.equals("")) {
            throw new CheckAuthException();
        }

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            request.setAttribute("adminId", Long.parseLong(claims.getPayload().getSubject()));

            return true;
        } catch (JwtException e) {
            throw new CheckAuthException();
        }
    }

}

