package com.roughsea.service;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.models.dto.MessageDto;
import com.roughsea.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private MessageRepository messageRepository;


    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {

        if (filter != null && !filter.isEmpty())
            return messageRepository.findByTag(filter, pageable, user);
        else
            return messageRepository.findAll(pageable, user);
    }

    public void saveFile(
            @RequestParam("file") MultipartFile file,
            @Valid Message message) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists())
                uploadDir.mkdir();

            String resultFileName =
                    UUID.randomUUID().toString() + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User author, User currentUser) {
        return messageRepository.findByUser(pageable, author, currentUser);
    }
}