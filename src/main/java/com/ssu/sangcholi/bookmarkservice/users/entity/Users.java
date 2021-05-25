package com.ssu.sangcholi.bookmarkservice.users.entity;

import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @Builder
    public Users(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}