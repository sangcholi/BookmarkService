package com.ssu.sangcholi.bookmarkservice.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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
}
