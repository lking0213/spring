package com.xahi.auditable;

import com.xahi.model.User;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Created by liwq on 2019/7/18 11:08
 */
public class AuditorAwareImpl implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        return null;
    }
}
