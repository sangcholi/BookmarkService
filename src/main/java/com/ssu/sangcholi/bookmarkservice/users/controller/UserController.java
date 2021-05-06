package com.ssu.sangcholi.bookmarkservice.users.controller;

import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import com.ssu.sangcholi.bookmarkservice.users.service.UserService;
import com.ssu.sangcholi.bookmarkservice.users.vo.RequestUser;
import com.ssu.sangcholi.bookmarkservice.users.vo.ResponseUser;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<EntityModel<ResponseUser>>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        List<EntityModel<ResponseUser>> body = users.stream()
                .map(user ->
                        EntityModel.of(
                                modelMapper.map(user, ResponseUser.class),
                                linkTo(methodOn(this.getClass()).getUserByUserId(user.getUserId())).withSelfRel(),
                                linkTo(methodOn(this.getClass()).getAllUsers()).withRel("all-users")
                        )
                ).collect(Collectors.toList());

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<ResponseUser>> getUserByUserId(String userId) {
        UserDto findUser = userService.getUserByUserId(userId);
        EntityModel<ResponseUser> body = EntityModel.of(
                modelMapper.map(findUser, ResponseUser.class),
                linkTo(methodOn(this.getClass()).getUserByUserId(findUser.getUserId())).withSelfRel(),
                linkTo(methodOn(this.getClass()).getAllUsers()).withSelfRel()
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        ResponseUser body = modelMapper.map(createdUser, ResponseUser.class);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).getUserByUserId(createdUser.getUserId())).toUri())
                .body(body);
    }
}
