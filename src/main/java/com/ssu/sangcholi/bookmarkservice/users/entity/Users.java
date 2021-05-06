package com.ssu.sangcholi.bookmarkservice.users.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public Users(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}