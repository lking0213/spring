package com.xahi;

import com.xahi.model.Role;
import com.xahi.model.RoleName;
import com.xahi.model.User;
import com.xahi.repository.RoleRepository;
import com.xahi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by liwq on 2019/7/18 10:29
 */
@Service
public class ApplicationSetup {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void init(){
        Arrays.stream(RoleName.values())
                .forEach(r->{
                    Role role = new Role();
                    role.setRoleName(r);
                    roleRepository.save(role);
                });

        Role roleAdmin = roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("role admin is not exist"));
        Role roleUser = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("role user is not exist"));


        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRoles(Collections.singleton(roleAdmin));
        repository.save(user);

        User user1 = new User();
        user1.setUsername("user");
        user1.setPassword(passwordEncoder.encode("user"));
        user1.setAccountNonExpired(true);
        user1.setAccountNonLocked(true);
        user1.setCredentialsNonExpired(true);
        user1.setEnabled(true);
        user1.setRoles(Collections.singleton(roleUser));
        repository.save(user1);
    }
}
