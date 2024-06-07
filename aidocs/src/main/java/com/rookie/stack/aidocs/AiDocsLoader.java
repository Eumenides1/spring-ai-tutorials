package com.rookie.stack.aidocs;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author eumenides
 * @description
 * @date 2024/6/3
 */
@Component
public class AiDocsLoader {
    private static  final Logger logger = LoggerFactory.getLogger(AiDocsLoader.class);
    private final JdbcClient jdbcClient;
    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;
    @Value("classpath:/docs")
    private Resource pdfResource;


    public AiDocsLoader(JdbcClient jdbcClient, VectorStore vectorStore, ResourceLoader resourceLoader) {
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
        this.resourceLoader = resourceLoader;
    }
    @PostConstruct
    public void init() throws IOException {
        Integer count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();
        logger.info("Current count of the Vector Store : {}", count);
        if (count == 0 ){
            logger.info("Loading Ai Docs PDF into Vector Store");
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(
                            new ExtractedTextFormatter.Builder()
                                    .withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();
            File docsDir = pdfResource.getFile();
            File[] pdfFiles = docsDir.listFiles((dir, name) -> name.endsWith(".pdf"));

            if (pdfFiles != null && pdfFiles.length > 0) {
                for (File pdfFile : pdfFiles) {
                    logger.info("Processing PDF file: {}", pdfFile.getName());
                    Resource pdfResource = resourceLoader.getResource("file:" + pdfFile.getAbsolutePath());
                    PagePdfDocumentReader documentReader = new PagePdfDocumentReader(pdfResource, config);
                    var textSplitter = new TokenTextSplitter();
                    vectorStore.accept(textSplitter.apply(documentReader.get()));
                    logger.info("{} is Ready!", pdfFile.getName());
                }
            }
            logger.info("Document is Ready!");
        }
    }
}


