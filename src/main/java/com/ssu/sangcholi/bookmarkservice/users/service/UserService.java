package com.ssu.sangcholi.bookmarkservice.users.service;

import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<UserDto> findAllUsers();

    public UserDto getUserByUserId(String userId);

    public UserDto createUser(UserDto userDto);
}
