package com.rookie.stack.springaitutorials.controller.image.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author eumenides
 * @description
 * @date 2024/6/13
 */
@Service
public class AIService {

    @Value("classpath:/prompts/judge.st")
    private Resource judgeTemplate;

    private final ChatClient chatClient;

    private  final OpenAiImageModel openaiImageModel;


    public AIService(ChatClient.Builder chatBuilder, OpenAiImageModel openaiImageModel) {
        this.chatClient = chatBuilder.build();
        this.openaiImageModel = openaiImageModel;
    }

    public String aiChat(String prompt) {

        return judge(prompt) ? image(prompt) : chat(prompt);
    }

    private String chat(String prompt) {
        Prompt chatPrompt = new Prompt(prompt);
        ChatResponse chatResponse = chatClient.prompt(chatPrompt).call().chatResponse();
        return chatResponse.getResult().getOutput().getContent();
    }

    private String image(String prompt) {
        ImagePrompt imagePrompt =
                new ImagePrompt(prompt, OpenAiImageOptions.builder()
                        .withResponseFormat("url")
                        .withWidth(1024)
                        .withHeight(1024)
                        .build());
        ImageGeneration result = openaiImageModel.call(imagePrompt).getResult();
        String url = result.getOutput().getUrl();
        return String.format("<img src='%s' alt='%s'>",url,prompt);
    }



    private boolean judge(String prompt) {
        // 构建提示词
        PromptTemplate promptTemplate = new PromptTemplate(judgeTemplate);
        Prompt p = promptTemplate.create(Map.of("prompt", prompt));
        ChatResponse chatResponse = chatClient.prompt(p).call().chatResponse();
        String judgeResult = chatResponse.getResult().getOutput().getContent();
        System.out.println(judgeResult);
        switch (judgeResult.toLowerCase()) {
            case "yes", "yes.":
                return true;
            default:
                return false;
        }
    }


}
