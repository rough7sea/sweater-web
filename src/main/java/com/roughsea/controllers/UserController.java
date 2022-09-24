package com.roughsea.controllers;

import com.roughsea.models.Role;
import com.roughsea.models.User;
import com.roughsea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user,
            Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){

        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(
            Model model,
            @AuthenticationPrincipal User currentUser){
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("email", currentUser.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestParam String password,
            @RequestParam String email
    ){

        userService.updateProfile(currentUser, password, email);

        return "redirect:/user/profile";
    }

    @GetMapping("/subscribe/{author}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author
    ){
        userService.subscribe(currentUser, author);


        return "redirect:/user-messages/" + author.getId();
    }

    @GetMapping("/unsubscribe/{author}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author
    ){
        userService.unsubscribe(currentUser, author);


        return "redirect:/user-messages/" + author.getId();
    }

    @GetMapping("{type}/{author}/list")
    public String userList(
            Model model,
            @PathVariable User author,
            @PathVariable String type
    ){

        model.addAttribute("userChannel", author);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type))
            model.addAttribute("users", author.getSubscriptions());
        else
            model.addAttribute("users", author.getSubscribers());

        return "subscriptions";
    }

}