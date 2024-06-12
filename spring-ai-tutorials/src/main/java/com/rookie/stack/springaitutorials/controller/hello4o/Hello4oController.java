package com.rookie.stack.springaitutorials.controller.hello4o;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author eumenides
 * @description
 * @date 2024/6/12
 */
@RestController
@RequestMapping("/hello4o")
public class Hello4oController {

    private final ChatClient chatClient;


    public Hello4oController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    @GetMapping("/code-joke")
    public String jokes(@RequestParam(value = "topic", defaultValue = "程序员")String topic){
        PromptTemplate promptTemplate = new PromptTemplate("给我讲一个关于{topic}的冷笑话，要很搞笑的那种");
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    }

    @GetMapping("/image-desc")
    public String describeImage() throws IOException {
        byte[] contentAsByteArray = new ClassPathResource("/images/2.png").getContentAsByteArray();
        UserMessage userMessage = new UserMessage("你是一个优秀的设计师和图像学家，你可以从专业的角度描述一下你看到的图片吗？",
                new Media(MimeTypeUtils.IMAGE_PNG, contentAsByteArray));

        return chatClient.prompt(new Prompt(userMessage)).call().chatResponse().getResult().getOutput().getContent();
    }

    @GetMapping("/code-desc")
    public String describeCode() throws IOException {
        byte[] contentAsByteArray = new ClassPathResource("/images/img.png").getContentAsByteArray();
        UserMessage userMessage = new UserMessage("你是一名优秀的高级程序员，请分析下图中的代码的功能，以及给出你的优化建议？",
                new Media(MimeTypeUtils.IMAGE_PNG, contentAsByteArray));

        return chatClient.prompt(new Prompt(userMessage)).call().chatResponse().getResult().getOutput().getContent();
    }


}
