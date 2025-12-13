//package com.app.tradeguess.security;
//
//import com.app.tradeguess.model.entity.User;
//import com.app.tradeguess.repository.UserRepository;
//import com.app.tradeguess.security.UserPrincipal;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByTelegramId(Long.parseLong(username))
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with telegramId: " + username));
//
//        return UserPrincipal.create(user);
//    }
//
//    @Transactional
//    public UserDetails loadUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with id: " + id));
//
//        return UserPrincipal.create(user);
//    }
//}