package com.xahi.web;

import com.xahi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liwq on 2019/7/18 11:20
 */
@RestController
@Slf4j
@RequestMapping("/w")
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    @PreAuthorize("ADMIN")
    public String save() {
        log.info("save...[{}], [{}]");

        return "abc";
    }
}
