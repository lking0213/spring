package com.xahi.auditable;

import com.xahi.model.User;
import com.xahi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by liwq on 2019/7/18 11:08
 */
@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        String name = authentication.getName();
        log.info("[{}]", name);
        return userRepository.findByUsername(name);
    }
}
