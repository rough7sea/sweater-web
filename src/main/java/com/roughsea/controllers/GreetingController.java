package com.roughsea.controllers;

import com.roughsea.models.Message;
import com.roughsea.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String home(Map<String, Object> model){

        Iterable<Message> messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }

    @PostMapping("/")
    public String add(
            @RequestParam String text, @RequestParam String tag,
            Model model){

        Message message = Message.builder()
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
            @RequestParam String tag, Map<String, Object> model){

        List<Message> messages;

        if (tag != null && !tag.isEmpty())
            messages = messageRepository.findByTag(tag);
        else
            messages = messageRepository.findAll();

        model.put("messages", messages);

        return "main";
    }

}