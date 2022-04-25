package com.syhrl.blog.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.syhrl.blog.entity.Mail;
import com.syhrl.blog.entity.PasswordForgot;
import com.syhrl.blog.entity.PasswordResetToken;
import com.syhrl.blog.entity.User;
import com.syhrl.blog.service.EmailService;
import com.syhrl.blog.service.PasswordResetTokenService;
import com.syhrl.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    private final UserService userService;
    private final MessageSource messageSource;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordController(UserService userService, MessageSource messageSource,
            PasswordResetTokenService passwordResetTokenService, EmailService emailService) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
    }

    @GetMapping
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@Valid @ModelAttribute("passwordForgot") PasswordForgot passwordForgot,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Forgot Password");
            return "forgot-password";
        }
        User user = userService.findByEmail(passwordForgot.getEmail());
        if (user == null) {
            model.addAttribute("emailError",
                    messageSource.getMessage("EMAIL_NOT_FOUND", new Object[] {}, Locale.ENGLISH));
            model.addAttribute("title", "Forgot Password");
            return "forgot-password";
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(30));
        passwordResetToken = passwordResetTokenService.savePasswordResetToken(passwordResetToken);

        if (passwordResetToken == null) {
            model.addAttribute("tokenError",
                    messageSource.getMessage("TOKEN_NOT_SAVED", new Object[] {}, Locale.ENGLISH));
            model.addAttribute("title", "Forgot Password");
            return "forgot-password";
        }

        Mail mail = new Mail();
        mail.setFrom("no-reply@myblog.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password Reset Request");

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("token", passwordResetToken);
        mailModel.put("user", user);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        mailModel.put("resetUrl", url + "/reset-password?token=" + passwordResetToken.getToken());
        mail.setModel(mailModel);
        emailService.send(mail);
        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("PASSWORD_RESET_TOKEN_SENT", new Object[] {}, Locale.ENGLISH));
        model.addAttribute("title", "Forgot Password");
        return "redirect:/forgot-password";
    }

    @ModelAttribute("passwordForgot")
    public PasswordForgot passwordForgot() {
        return new PasswordForgot();
    }
}
