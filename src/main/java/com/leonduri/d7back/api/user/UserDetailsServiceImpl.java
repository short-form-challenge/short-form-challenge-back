package com.leonduri.d7back.api.user;


import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userPk) {
        return repository.findById(Long.valueOf(userPk))
                .orElseThrow(CUserNotFoundException::new);
    }
}