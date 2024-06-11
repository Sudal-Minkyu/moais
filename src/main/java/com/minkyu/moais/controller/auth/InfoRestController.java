package com.minkyu.moais.controller.auth;

import com.minkyu.moais.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "Info API", description = "내정보 관련 API")
@RestController
@RequestMapping("/moais/v2/api/info")
public class InfoRestController {

    private final InfoService infoService;

    @Autowired
    public InfoRestController(InfoService infoService){
        this.infoService = infoService;
    }

    @Operation(summary = "회원탈퇴 API", description = "계정을 탈퇴한다.")
    @PostMapping("/secession")
    public ResponseEntity<Map<String,Object>> secession(HttpServletRequest request) {
        return infoService.secession(request);
    }

}