package com.learngerman.helper.controller;

import com.learngerman.helper.service.OllamaChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stockmarket")
public class AiController {

    @Autowired
    private final OllamaChatService chatService;

    public AiController(OllamaChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/info")
    public String faq(@RequestParam(value = "message") String message ) {
        return chatService.call(message);
    }

}
