package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.entity.User;
import com.example.quanly_vlxd.model.UserCustomDetail;
import com.example.quanly_vlxd.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findbyUser= userRepo.findByUserName(username);
        return UserCustomDetail.builder().user(findbyUser.orElse(null)).build();
    }
}
