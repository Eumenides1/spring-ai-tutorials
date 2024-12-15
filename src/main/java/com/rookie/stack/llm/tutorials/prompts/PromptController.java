package com.rookie.stack.llm.tutorials.prompts;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private final OpenAiChatModel chatModel;

    @Autowired
    public PromptController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }
    @GetMapping("/simple")
    public String simplePrompts(){
        PromptTemplate promptTemplate = new PromptTemplate("我是一名 {language} 程序员，给我出一道关于 {topic}的面试题。");

        Prompt prompt = promptTemplate.create(Map.of("language", "java", "topic", "计算机网络"));

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    @GetMapping("/role")
    public String rolePrompts(){
        String userText = """
                我是一个从事开发三年的 Java 程序员，擅长 Java，Spring Boot，计算机网络，MySQL 相关开发。
                """;
        Message userMessage = new UserMessage(userText);

        String systemText = """
                你是一个高级程序员面试官，你会根据用户的职业经历，为用户提供模拟面试。
                你的名字是:{name},
                你需要根据用户提供的个人信息，解析用户从事的开发方向以及用户擅长的的技术栈，针对用户擅长的技术栈，出{num}道面试题。
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "超级面试官", "num", 3));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        List<Generation> response = chatModel.call(prompt).getResults();
        return response.toString();
    }


}
