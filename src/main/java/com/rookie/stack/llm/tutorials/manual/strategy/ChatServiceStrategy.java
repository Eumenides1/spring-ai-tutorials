package com.rookie.stack.llm.tutorials.manual.strategy;

import org.springframework.ai.chat.model.ChatModel;

import java.util.Map;

public interface ChatServiceStrategy {
    ChatModel createChatModel(Map<String, String> headers);
}
