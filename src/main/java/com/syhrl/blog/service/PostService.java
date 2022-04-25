package com.syhrl.blog.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.syhrl.blog.entity.Post;
import com.syhrl.blog.entity.User;


public interface PostService {
    Collection<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> getPost(Long id);
    List<Post> getPostByUser(User user);
    Optional<Post> getPostBySlug(String slug);
    List<Post> getPosts(String keyword);
    void savePost(Post post);
    void deletePost(Long id);

}
