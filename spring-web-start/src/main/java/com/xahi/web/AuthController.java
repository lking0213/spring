package com.xahi.web;

import com.xahi.dto.LoginUserDTO;
import com.xahi.util.Constants;
import com.xahi.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwq on 2019/7/18 16:10
 */
@RestController
@RequestMapping("/pub")
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> save(@RequestBody LoginUserDTO loginUserDTO, HttpServletRequest request){
        log.info("login...[{}], [{}]", loginUserDTO.getUsername(), loginUserDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(),
                loginUserDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        Map<String, Object> map = new HashMap<>(4);
        map.put("username", authenticate.getName());
        String token = JwtUtil.doGenerateToken(map);

        return ResponseEntity.created(null).header(Constants.AUTHORIZATION, token).body("Hello World");
    }
}
