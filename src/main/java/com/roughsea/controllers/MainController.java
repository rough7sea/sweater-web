package com.roughsea.controllers;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String home(Map<String, Object> model){

        Iterable<Message> messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model){

        Message message = Message.builder()
                .author(user)
                .text(text)
                .tag(tag)
                .build();

        messageRepository.save(message);

        Iterable<Message> messages = messageRepository.findAll();

        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/filter")
    public String filter(
            @RequestParam String filter, Map<String, Object> model){

        List<Message> messages;

        if (filter != null && !filter.isEmpty())
            messages = messageRepository.findByTag(filter);
        else
            messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }
}