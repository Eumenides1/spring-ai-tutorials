package com.rookie.stack.llm.tutorials.manual.strategy.impl;

import com.rookie.stack.llm.tutorials.manual.strategy.ChatServiceStrategy;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.Map;

public class OpenAiChatServiceStrategy implements ChatServiceStrategy {
    @Override
    public ChatModel createChatModel(Map<String, String> headers) {
        String apiKey = headers.getOrDefault("API-Key", System.getenv("MY_API_KEY"));
        String model = headers.getOrDefault("Model", "gpt-3.5-turbo");
        double temperature = Double.parseDouble(headers.getOrDefault("Temperature", "0.7"));
        int maxTokens = Integer.parseInt(headers.getOrDefault("Max-Tokens", "200"));

        var openAiApi = new OpenAiApi(System.getenv("OPENAI_API_BASE"), apiKey);
        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature)
                .withMaxTokens(maxTokens)
                .build();
        return new OpenAiChatModel(openAiApi, openAiChatOptions);
    }
}
