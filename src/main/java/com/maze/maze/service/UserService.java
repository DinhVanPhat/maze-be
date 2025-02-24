package com.maze.maze.service;

import java.util.HashSet;
import com.maze.maze.model.Account;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maze.maze.repository.AccountRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Account ac = accountRepository.findByPhoneNumber(phoneNumber);
        String password = ac.getPassword();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (ac.getRole().name() == "admin") {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } 
        else if (ac.getRole().name() == "seller") {
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        }
        else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(phoneNumber, password, authorities);

    }
}
