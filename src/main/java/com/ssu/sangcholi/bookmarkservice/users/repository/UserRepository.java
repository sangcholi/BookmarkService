package com.ssu.sangcholi.bookmarkservice.users.repository;

import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUserId(String userId);
}
