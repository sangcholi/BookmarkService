package com.ssu.sangcholi.bookmarkservice.bookmark.domain;

import com.ssu.sangcholi.bookmarkservice.users.entity.Users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String original;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summarization;
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
