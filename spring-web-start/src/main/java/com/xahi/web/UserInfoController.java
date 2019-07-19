package com.xahi.web;

import com.xahi.model.User;
import com.xahi.repository.UserRepository;
import com.xahi.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String save(@RequestBody User user) {
        log.info("save...[{}], [{}]");
        userRepository.save(user);
        return "ok";
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String get(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("save...[{}], [{}]", userPrincipal.getUsername(), authentication.getCredentials());
//        log.info("save...[{}], [{}]");

        return "ok";
    }
}
