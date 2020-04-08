package com.roughsea.controllers;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller

public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String home(
            @RequestParam(required = false, defaultValue = "")String filter,
            Model model){

        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty())
            messages = messageRepository.findByTag(filter);
        else
            messages = messageRepository.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model
    ) throws IOException {

        Message message = Message.builder()
                .author(user)
                .text(text)
                .tag(tag)
                .build();

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists())
                uploadDir.mkdir();

            String resultFileName =
                    UUID.randomUUID().toString() + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
        messageRepository.save(message);

        Iterable<Message> messages = messageRepository.findAll();

        model.addAttribute("messages", messages);
        return "main";
    }
}