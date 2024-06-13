package com.rookie.stack.springaitutorials.controller.image.controller;

import com.rookie.stack.springaitutorials.controller.image.service.AIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eumenides
 * @description
 * @date 2024/6/13
 */
@RestController
public class ImageController {

    private final AIService aiService;

    public ImageController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/image")
    public String chat(@RequestParam(value = "prompt") String prompt) {
        return aiService.aiChat(prompt);
    }
}
