package com.ssu.sangcholi.bookmarkservice.bookmark.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestBookmark {
    @NotNull
    @NotBlank
    public String original;
    @NotNull
    @NotBlank
    public String summarization;
}
