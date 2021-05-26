package com.ssu.sangcholi.bookmarkservice.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(exclude = {"id"})
public class BookmarkDto {
    public Long id;
    public String original;
    public String summarization;
    public LocalDateTime createAt;
}
