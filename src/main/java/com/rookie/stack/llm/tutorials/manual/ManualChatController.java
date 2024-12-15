package com.rookie.stack.llm.tutorials.manual;

import com.rookie.stack.llm.tutorials.manual.strategy.factory.ChatServiceFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
public class ManualChatController {
    private final ChatServiceFactory chatServiceFactory;

    public ManualChatController() {
        this.chatServiceFactory = new ChatServiceFactory();
    }

    @GetMapping("/manual_header")
    public String chat(@RequestHeader Map<String, String> headers, @RequestParam String prompt) {
        try {
            // 获取聊天服务
            ChatModel chatService = chatServiceFactory.getService(headers);
            // 调用聊天服务并返回结果
            ChatResponse response = chatService.call(new Prompt(prompt));
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            // 处理错误
            return "Error: " + e.getMessage();
        }
    }
}
