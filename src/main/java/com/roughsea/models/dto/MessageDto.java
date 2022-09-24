package com.roughsea.models.dto;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.models.util.MessageHelper;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"text","tag","filename"})
public class MessageDto {
    private Long id;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long likes;
    private boolean meLiked;

    public MessageDto(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.tag = message.getTag();
        this.author = message.getAuthor();
        this.filename = message.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName(){
        return MessageHelper.getAuthorName(author);
    }
}
