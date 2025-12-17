package com.app.tradeguess.security;

import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return createUserDetails(user);
    }

    public UserDetails loadUserByTelegramId(Long telegramId) throws UsernameNotFoundException {
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with telegramId: " + telegramId));

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}