package com.ssu.sangcholi.bookmarkservice.users.controller;

import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import com.ssu.sangcholi.bookmarkservice.users.service.UserService;
import com.ssu.sangcholi.bookmarkservice.users.vo.RequestUser;
import com.ssu.sangcholi.bookmarkservice.users.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/checkid/{userId}")
    public ResponseEntity checkUserId(@PathVariable String userId) {
        userService.checkUserId(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUserByUserId(@PathVariable String userId) {
        UserDto findUser = userService.getUserByUserId(userId);
        ResponseUser body = new ResponseUser(findUser);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseUser> createUser(@RequestBody @Valid RequestUser requestUser, Errors error) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        ResponseUser body = new ResponseUser(createdUser);

        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).getUserByUserId(createdUser.getUserId())).toUri())
                .body(body);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
