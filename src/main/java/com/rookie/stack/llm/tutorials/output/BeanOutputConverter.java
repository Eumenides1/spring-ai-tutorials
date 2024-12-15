package com.rookie.stack.llm.tutorials.output;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/converter")
public class BeanOutputConverter {

    private final OpenAiChatModel chatModel;

    @Autowired
    public BeanOutputConverter(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/bean")
    public String beanOutput() {
        DevLanguage devLanguage = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("生成{topic}主题相关的{num}道面试要点")
                        .param("topic","计算机网络")
                        .param("num","3"))
                .call()
                .entity(DevLanguage.class);
        return devLanguage.toString();
    }
    @GetMapping("/reference")
    public String typeReference(){
        List<DevLanguage> devLanguageList = ChatClient.create(chatModel).prompt()
                .user(u->u.text("生成计算机网络，SpringBoot 两个主题相关的{num}道面试要点")
                        .param("num", "3"))
                .call()
                .entity(new ParameterizedTypeReference<List<DevLanguage>>() {});
        return devLanguageList.toString();
    }
    @GetMapping("/map")
    public Map<String, Object> mapOutPut() {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("给我提供一个 {subject} 列表")
                        .param("subject", "一个 Key 是 language 的数组，数组内容是 10种开发语言"))
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    @GetMapping("/list")
    public List<String> listOutPut() {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("给我一共5个 {subject}")
                        .param("subject", "Go 语言面试要点"))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }
}
// 通过 @JsonPropertyOrder 注解在生成的 JSON Schema 中自定义属性排序。
@JsonPropertyOrder({"category", "topic"})
record DevLanguage(String category, List<String> topic) {
}
