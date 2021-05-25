package com.ssu.sangcholi.bookmarkservice.users.repository;

import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUserId(String userId);

    @Query("select user.id from Users user where user.userId = :userId")
    public Optional<Long> findIdByUserId(String userId);
}
