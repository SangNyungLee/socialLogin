package com.example.socialLogin.controller;

import com.example.socialLogin.service.GithubLoginService;
import com.example.socialLogin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitLoginController {

    @Autowired
    GithubLoginService githubLoginService;

    @GetMapping("/callback")
    public void getUerInfo(@RequestParam String code){
        System.out.println("코드는? " + code);
        githubLoginService.gitLogin(code);
    }
}
