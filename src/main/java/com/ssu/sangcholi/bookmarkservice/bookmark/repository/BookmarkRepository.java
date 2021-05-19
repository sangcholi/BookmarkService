package com.ssu.sangcholi.bookmarkservice.bookmark.repository;

import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("select bm from Bookmark bm where bm.user.userId = :userId and bm.id = :id")
    public Optional<Bookmark> findByUserAndId(String userId, Long id);
}
