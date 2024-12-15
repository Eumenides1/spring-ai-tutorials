package com.rookie.stack.llm.tutorials.manual.strategy.factory;

import com.rookie.stack.llm.tutorials.manual.strategy.ChatServiceStrategy;
import com.rookie.stack.llm.tutorials.manual.strategy.impl.OpenAiChatServiceStrategy;
import com.rookie.stack.llm.tutorials.manual.strategy.impl.QianFanChatServiceStrategy;
import org.springframework.ai.chat.model.ChatModel;

import java.util.HashMap;
import java.util.Map;

public class ChatServiceFactory {
    private final Map<String, ChatServiceStrategy> strategies;

    public ChatServiceFactory() {
        strategies = new HashMap<>();
        strategies.put("openai", new OpenAiChatServiceStrategy());
        strategies.put("qianfan", new QianFanChatServiceStrategy());
    }

    public ChatModel getService(Map<String, String> headers) {
        // 获取模型类型（默认 openai）
        String modelType = headers.getOrDefault("Model-Type", "openai").toLowerCase();

        // 找到对应策略
        ChatServiceStrategy strategy = strategies.get(modelType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported model type: " + modelType);
        }

        // 根据策略创建服务
        return strategy.createChatModel(headers);
    }
}