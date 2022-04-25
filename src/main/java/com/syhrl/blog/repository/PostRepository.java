package com.syhrl.blog.repository;

import java.util.List;
import java.util.Optional;

import com.syhrl.blog.entity.Post;
import com.syhrl.blog.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    Optional<Post> findBySlug(String slug);
    List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> search(String keyword);
}
