package com.xahi.security;

import com.xahi.model.User;
import com.xahi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by liwq on 2019/7/18 14:16
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : " + username));

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        Collection<SimpleGrantedAuthority> authorities =
                user.getRoles().stream().map(r->new SimpleGrantedAuthority(r.getRoleName().name())).collect(Collectors.toList());
        return new UserPrincipal(authorities, user.getUsername(), user.getPassword(),
                user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(),
                user.isEnabled());
    }
}
