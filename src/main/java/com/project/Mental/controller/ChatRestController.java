package com.project.Mental.controller;
import com.project.Mental.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api")
public class ChatRestController {
    @Autowired private GeminiService geminiService;
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        return Map.of("reply", geminiService.getChatResponse(payload.get("message")));
    }
}