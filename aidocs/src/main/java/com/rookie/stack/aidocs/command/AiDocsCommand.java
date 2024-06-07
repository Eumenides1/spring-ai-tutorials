package com.rookie.stack.aidocs.command;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eumenides
 * @description
 * @date 2024/6/7
 */
@Command
public class AiDocsCommand {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/ai-docs.st")
    private Resource aiDocsTemplate;

    public AiDocsCommand(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    @Command(command = "q")
    public String question(@DefaultValue(value = "影子大人的真实身份是？") String message) {
        PromptTemplate promptTemplate = new PromptTemplate(aiDocsTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));

        return chatClient.prompt(promptTemplate.create(promptParameters)).call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }

    private List<String> findSimilarDocuments(String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
        return similarDocuments.stream().map(Document::getContent).toList();
    }

}
