package com.syhrl.blog.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.syhrl.blog.entity.User;
import com.syhrl.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;
    private final MessageSource messageSource;

    @Autowired
    public SignupController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Autowired
    protected AuthenticationManager authenticationManager;

    @GetMapping
    public String signup(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("title", "Signup");
            return "signup";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", messageSource.getMessage("EMAIL_EXISTS", new Object[] {}, Locale.ENGLISH));
        }
        user = userService.save(user);
        if (user == null) {
            model.addAttribute("error", messageSource.getMessage("EMAIL_NOT_SAVED", new Object[] {}, Locale.ENGLISH));
        }
        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("EMAIL_SAVED", new Object[] {}, Locale.ENGLISH));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }

    @ModelAttribute("user")
    public User user() {
        return new User();
    }
}
