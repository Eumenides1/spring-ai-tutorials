package com.rookie.stack.springaitutorials.controller.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author eumenides
 * @description
 * @date 2024/6/2
 */
@Component
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);


    @Value("classpath:/docs/qinyunian.txt")
    private Resource faq;

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("Vector Store file exists");
            simpleVectorStore.load(vectorStoreFile);
        } else {
            log.info("Vector Store file dose not exist, loading documents");
            TextReader textReader = new TextReader(faq);
            textReader.getCustomMetadata().put("filename","qinyunian.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> apply = tokenTextSplitter.apply(documents);
            simpleVectorStore.add(apply);
            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }


}
