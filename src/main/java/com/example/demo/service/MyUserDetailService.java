package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Users;
import com.example.demo.model.UserPrinciple;
import com.example.demo.repo.UserRepo;

@Service
public class MyUserDetailService implements UserDetailsService {

    UserRepo userRepo;

    @Autowired
    public MyUserDetailService(UserRepo userReop) {
        this.userRepo = userReop;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);

        if(user == null) {
            System.out.println("user not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrinciple(user);
    }
    
}
