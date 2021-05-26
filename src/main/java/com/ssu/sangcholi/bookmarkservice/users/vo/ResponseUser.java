package com.ssu.sangcholi.bookmarkservice.users.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import lombok.Data;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser extends EntityModel<UserDto> {
    public Long id;
    public String userId;

    public ResponseUser(UserDto userDto) {
        this.id = userDto.getId();
        this.userId = userDto.getUserId();
    }

}
