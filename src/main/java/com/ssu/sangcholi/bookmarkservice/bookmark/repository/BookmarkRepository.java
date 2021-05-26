package com.ssu.sangcholi.bookmarkservice.bookmark.repository;

import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("select bm from Bookmark bm join bm.user user where user.userId = :userId and bm.id = :id")
    public Optional<Bookmark> findByUserAndId(String userId, Long id);

    @Query("select bm from Bookmark bm join bm.user user where user.userId = :userId")
    public List<Bookmark> findAllByUserId(String userId);

    @Query(value = "select bm from Bookmark bm join bm.user user where user.userId = :userId")
    public Page<Bookmark> findAllByUserId(String userId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from Bookmark bm where bm.user = :user")
    public void deleteByUserId(Users user);
}
