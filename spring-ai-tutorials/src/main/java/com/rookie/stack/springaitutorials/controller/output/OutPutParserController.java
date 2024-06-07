package com.rookie.stack.springaitutorials.controller.output;


import com.rookie.stack.springaitutorials.data.Movies;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author eumenides
 * @description
 * @date 2024/5/30
 */
@RestController
public class OutPutParserController {

    private final ChatClient chatClient;


    public OutPutParserController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping("list_movie")
    public List<String> getHistoryPopularMovieByList(@RequestParam(value = "category",
            defaultValue = "悬疑") String category) {
        String message = """
                列出你认为的世界上最好的 10部{category}电影，带上他们上映的年份；
                并给出推荐理由，只返回结果，不返回任何无用的描述，如果返回其他信息会受到处罚;
                
                {format}
                """;
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());
        PromptTemplate promptTemplate = new PromptTemplate(message,Map.of("category", category, "format" ,listOutputConverter.getFormat()));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatClient.prompt(prompt).call().chatResponse().getResult();
        List<String> list = listOutputConverter.convert(generation.getOutput().getContent());
        return list;
    }

    @GetMapping("map_movie")
    public Map<String, Object> getHistoryPopularMovieByMap(@RequestParam(value = "category",
            defaultValue = "悬疑") String category) {
        MapOutputConverter outputConverter = new MapOutputConverter();
        String format = outputConverter.getFormat();
        String message = """
                列出你认为的世界上最好的 10部{category}电影，带上他们上映的年份；
                并给出推荐理由，以JSON格式以及中文内容返回，并且只返回结果，不返回任何无用的描述，如果返回其他信息会受到处罚;
                
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message,Map.of("category", category, "format" ,format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatClient.prompt(prompt).call().chatResponse().getResult();
        Map<String, Object> map = outputConverter.convert(generation.getOutput().getContent());
        return map;
    }

    @GetMapping("bean_movie")
    public Movies getHistoryPopularMovieByBean(@RequestParam(value = "category",
            defaultValue = "悬疑") String category) {

        BeanOutputConverter<Movies> outputConverter = new BeanOutputConverter<>(Movies.class);
        String format = outputConverter.getFormat();
        String message = """
                列出你认为的世界上最好的 10部{category}电影，带上他们上映的年份；
                并给出推荐理由，以JSON格式以及中文内容返回，并且只返回结果，不返回任何无用的描述，如果返回其他信息会受到处罚;
                
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message,Map.of("category", category, "format" ,format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatClient.prompt(prompt).call().chatResponse().getResult();
        return outputConverter.convert(generation.getOutput().getContent());
    }


}
