package com.minkyu.moais.controller.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
public class UserController {

    @GetMapping
    public RedirectView rootRedirect() {
        return new RedirectView("/login");
    }

    @RequestMapping("login")
    public String login(){
        return "login";
    }

}
