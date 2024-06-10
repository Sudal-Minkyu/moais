package com.minkyu.moais;

import com.minkyu.moais.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class MoaisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoaisApplication.class, args);
    }

}
