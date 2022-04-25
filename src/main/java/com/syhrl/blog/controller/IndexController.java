package com.syhrl.blog.controller;

import com.syhrl.blog.entity.Post;
import com.syhrl.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;

@Controller
public class IndexController {
    private final PostService postService;

    public IndexController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Collection<Post> post = postService.findAllByOrderByCreatedAtDesc();
        model.addAttribute("title", "Home");
        model.addAttribute("post", post);
        return "index";
    }

    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("title", "Login");
        return principal != null ? "redirect:/" : "login";
    }
}
