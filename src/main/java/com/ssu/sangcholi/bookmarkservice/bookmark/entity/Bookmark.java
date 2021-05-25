package com.ssu.sangcholi.bookmarkservice.bookmark.entity;

import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String original;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summarization;
    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public void saveUser(Users user) {
        user.getBookmarkList().add(this);
        this.user = user;
    }

    @Builder
    public Bookmark(String original, String summarization) {
        this.original = original;
        this.summarization = summarization;
    }
}
