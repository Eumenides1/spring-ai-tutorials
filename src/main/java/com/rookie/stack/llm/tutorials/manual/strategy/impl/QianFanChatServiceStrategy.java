package com.rookie.stack.llm.tutorials.manual.strategy.impl;

import com.rookie.stack.llm.tutorials.manual.strategy.ChatServiceStrategy;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.qianfan.QianFanChatModel;
import org.springframework.ai.qianfan.QianFanChatOptions;
import org.springframework.ai.qianfan.api.QianFanApi;

import java.util.Map;

public class QianFanChatServiceStrategy implements ChatServiceStrategy {
    @Override
    public ChatModel createChatModel(Map<String, String> headers) {
        String apiKey = headers.getOrDefault("API-Key", System.getenv("QIANFAN_API_KEY"));
        String model = headers.getOrDefault("Model", QianFanApi.ChatModel.ERNIE_Speed_8K.getValue());
        double temperature = Double.parseDouble(headers.getOrDefault("Temperature", "0.4"));
        int maxTokens = Integer.parseInt(headers.getOrDefault("Max-Tokens", "200"));

        var qianFanApi = new QianFanApi(System.getenv("QIANFAN_API_BASE"), apiKey);
        var qianFanChatOptions = QianFanChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature)
                .withMaxTokens(maxTokens)
                .build();
        return new QianFanChatModel(qianFanApi, qianFanChatOptions);
    }
}
