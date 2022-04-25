package com.syhrl.blog.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.github.slugify.Slugify;
import com.syhrl.blog.entity.Post;
import com.syhrl.blog.entity.User;
import com.syhrl.blog.service.PostService;
import com.syhrl.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/blog")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        Collection<Post> post = postService.findAllByOrderByCreatedAtDesc();
        model.addAttribute("title", "Blog");
        model.addAttribute("post", post);
        return "blog/index";
    }

    @GetMapping("/search")
    public String search(String keyword, Model model) {
        List<Post> post = postService.getPosts(keyword);
        model.addAttribute("title", "Search");
        model.addAttribute("keyword", keyword);
        model.addAttribute("post", post);
        return "blog/index";
    }
    
    @GetMapping("/create")
    public String post(Model model) {
        Post post = new Post();
        model.addAttribute("title", "Buat Postingan");
        model.addAttribute("post", post);
        return "blog/create";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Post post, BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes
                            ) throws IOException {
        if(bindingResult.hasErrors()){
            return "redirect:/blog/create";
        }

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(principal.getName());

        String title = post.getTitle();
        String slug = new Slugify().slugify(title);

        UploadFileHandler(post, file, model);

        post.setSlug(slug);
        post.setUser(user);

        postService.savePost(post);


        redirectAttributes.addFlashAttribute("success", "Postingan berhasil dibuat");
        return "redirect:/blog/my";
    }

    public void UploadFileHandler (@Valid Post post, @RequestParam MultipartFile file, Model model){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadDirectory + file.getOriginalFilename());
                Files.write(path, bytes);
                post.setImgPath(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/{slug}/edit")
    public String edit(Model model, @PathVariable("slug") String slug, Principal principal) {
        Optional<Post> post = postService.getPostBySlug(slug);
        if (post.isPresent()) {
            if (principal.getName().equals(post.get().getUser().getEmail())) {
                model.addAttribute("title", "Edit Post");
                model.addAttribute("post", post.get());
                return "blog/edit";
            }else {
                return "redirect:/";
            }
        }else {
            return "redirect:/";
        }
        
    }

    @RequestMapping(value = "/{slug}/edit", method = RequestMethod.POST)
    public String update(Post post, Model model, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes 
                            ) throws IOException {


        Post postUpdate = postService.getPostBySlug(post.getSlug()).get();

        if(!file.isEmpty()){
            UploadFileHandler(postUpdate, file, model);
        }
        postUpdate.setTitle(post.getTitle());
        postUpdate.setContent(post.getContent());
        Slugify slugify = new Slugify();
        String slug = slugify.slugify(post.getTitle());
        postUpdate.setSlug(slug);
        postUpdate.setUpdatedAt(post.getUpdatedAt());
        postService.savePost(postUpdate);
        redirectAttributes.addFlashAttribute("success", "Postingan berhasil diedit");
        return "redirect:/blog/my";
    }
        
    @GetMapping("/my")
    public String myPost(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Post> posts = postService.getPostByUser(user);
        model.addAttribute("title", "Postingan saya");
        model.addAttribute("posts", posts);
        return "blog/my";
    }

    @GetMapping("/{slug}")
    public String show(Model model, @PathVariable("slug") String slug) {
        Optional<Post> post = postService.getPostBySlug(slug);
        if (post.isPresent()) {
            model.addAttribute("title", post.get().getTitle());
            model.addAttribute("post", post.get());
            return "blog/post";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes) throws IOException {
        Optional<Post> post = postService.getPost(id);
        if (post.isPresent()) {
            if (principal.getName().equals(post.get().getUser().getEmail())) {
                Path path = Paths.get(uploadDirectory + post.get().getImgPath());
                Files.delete(path);
                postService.deletePost(id);
                redirectAttributes.addFlashAttribute("success", "Postingan berhasil dihapus");
                return "redirect:/blog/my";
            }else {
                return "redirect:/";
            }
        }else {
            return "redirect:/";
        }
    }
}
