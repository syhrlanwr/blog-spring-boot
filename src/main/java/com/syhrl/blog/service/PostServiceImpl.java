package com.syhrl.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.syhrl.blog.entity.Post;
import com.syhrl.blog.entity.User;
import com.syhrl.blog.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Collection<Post> findAllByOrderByCreatedAtDesc() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }
    
    @Override
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }
    
    @Override
    public List<Post> getPostByUser(User user){
        return postRepository.findByUser(user);
    }

    @Override
    public List<Post> getPosts(String keyword) {
        return postRepository.search(keyword);
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        
    }
}
