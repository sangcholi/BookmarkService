package com.ssu.sangcholi.bookmarkservice.bookmark.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssu.sangcholi.bookmarkservice.bookmark.controller.BookmarkController;
import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBookmark extends EntityModel<BookmarkDto> {
    public String original;
    public String summarization;

    public ResponseBookmark(String userId,BookmarkDto content) {
        super(content);
        add(linkTo(BookmarkController.class).slash(userId).slash(content.getId()).withSelfRel());
        add(linkTo(BookmarkController.class).slash(userId).slash(content.getId()).withRel("delete"));
    }
}
