package com.rookie.stack.llm.tutorials.manual;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.qianfan.QianFanChatModel;
import org.springframework.ai.qianfan.QianFanChatOptions;
import org.springframework.ai.qianfan.api.QianFanApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手动配置 OpenAI 客户端
 */
@RestController
@RequestMapping("/manual/open-ai")
public class OpenAIManualController {

    @GetMapping("/generate")
    public String openAI() {
        var openAiApi = new OpenAiApi(System.getenv("OPENAI_API_BASE"),
                System.getenv("MY_API_KEY"));
        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo")
                .withTemperature(0.4)
                .withMaxTokens(200)
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions);

        ChatResponse response = chatModel.call(
                new Prompt("Generate the names of 5 famous pirates."));

//        // Or with streaming responses
//        Flux<ChatResponse> response = chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
        return response.getResult().getOutput().getText();
    }
    @GetMapping("/qianFan")
    public String qianFan() {
        var qianFanApi = new QianFanApi(System.getenv("OPENAI_API_BASE"),
                System.getenv("MY_API_KEY"));
        var qianFanChatOptions = QianFanChatOptions.builder()
                .withModel(QianFanApi.ChatModel.ERNIE_Speed_8K.getValue())
                .withTemperature(0.4)
                .withMaxTokens(200)
                .build();
        var chatModel = new QianFanChatModel(qianFanApi, qianFanChatOptions);

        ChatResponse response = chatModel.call(
                new Prompt("Generate the names of 5 famous pirates."));

//        // Or with streaming responses
//        Flux<ChatResponse> response = chatModel.stream(
//                new Prompt("Generate the names of 5 famous pirates."));
        return response.getResult().getOutput().getText();
    }
}
