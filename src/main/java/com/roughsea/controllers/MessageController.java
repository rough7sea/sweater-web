package com.roughsea.controllers;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.models.dto.MessageDto;
import com.roughsea.repositories.MessageRepository;
import com.roughsea.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String home(
            @RequestParam(required = false, defaultValue = "")String filter,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model
            ){

        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam(required = false, defaultValue = "") String filter,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {

        message.setAuthor(user);

        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        }else {

            messageService.saveFile(file, message);

            model.addAttribute("message", null);

            messageRepository.save(message);
        }

        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("url", "/main");
        model.addAttribute("page", page);

        return "main";
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<MessageDto> page = messageService.messageListForUser(pageable, author, currentUser);

        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("userChannel", author);

        model.addAttribute("page",page);
        model.addAttribute("message",message);

        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url","/user-messages/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{author}")
    public String updateMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long author,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (message.getAuthor().equals(currentUser)){
            if (!StringUtils.isEmpty(text))
                message.setText(text);

            if (!StringUtils.isEmpty(tag))
                message.setTag(tag);

            messageService.saveFile(file, message);

            messageRepository.save(message);
        }

        return "redirect:/user-messages/" + author;
    }

    @GetMapping("/message/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ){
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)){
            likes.remove(currentUser);
        }else
            likes.add(currentUser);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .forEach(redirectAttributes::addAttribute);

        return "redirect:" + components.getPath();
    }

}