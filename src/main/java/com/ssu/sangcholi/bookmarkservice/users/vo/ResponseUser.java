package com.ssu.sangcholi.bookmarkservice.users.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssu.sangcholi.bookmarkservice.users.controller.UserController;
import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser extends EntityModel<UserDto> {
    public ResponseUser(UserDto userDto) {
        super(userDto);
        userDto.setPassword(null);
        add(linkTo(UserController.class).slash(userDto.getUserId()).withSelfRel());
    }

}
