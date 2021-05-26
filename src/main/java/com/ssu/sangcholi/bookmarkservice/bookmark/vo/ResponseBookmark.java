package com.ssu.sangcholi.bookmarkservice.bookmark.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBookmark {
    public Long id;
    public String original;
    public String summarization;
    public LocalDateTime createAt;

    public ResponseBookmark(BookmarkDto content) {
        this.id = content.getId();
        this.original = content.getOriginal();
        this.summarization = content.getSummarization();
        this.createAt = content.getCreateAt();
    }
}
