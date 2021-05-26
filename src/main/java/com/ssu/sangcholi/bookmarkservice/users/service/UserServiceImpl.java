package com.ssu.sangcholi.bookmarkservice.users.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.repository.BookmarkRepository;
import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import com.ssu.sangcholi.bookmarkservice.users.exception.DuplicatedIdException;
import com.ssu.sangcholi.bookmarkservice.users.exception.NotFindUserException;
import com.ssu.sangcholi.bookmarkservice.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Users findUser = userRepository.findByUserId(userId)
                .orElseThrow(NotFindUserException::new);
        return modelMapper.map(findUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        Users newUser = Users.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        if (userRepository.findIdByUserId(newUser.getUserId()).isPresent()) {
            throw new DuplicatedIdException();
        }
        Users returnUser = userRepository.save(newUser);
        return modelMapper.map(returnUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(NotFindUserException::new);
        return new User(user.getUserId(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public void checkUserId(String userId) {
        Optional<Users> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isPresent()) {
            throw new DuplicatedIdException();
        }
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        Users user = userRepository.findByUserId(userId).orElseThrow(NotFindUserException::new);
        bookmarkRepository.deleteByUserId(user);
        userRepository.deleteOptimal(user);
    }
}
