package com.rookie.stack.llm.tutorials.multiModal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multiModal")
public class MultiModal {

    private final OpenAiChatModel chatModel;

    @Autowired
    public MultiModal(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("image")
    public String image(){
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("描述一下你看到的图?")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("static/green_girl.png")))
                .call()
                .content();
    }

}
