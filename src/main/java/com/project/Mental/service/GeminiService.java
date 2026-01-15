package com.project.Mental.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService {
    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.url}")
    private String apiUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getChatResponse(String userMessage) {
        try {
            String fullPrompt = "You are Calmi, a supportive mental health assistant. User says: " + userMessage;
            String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapeJson(fullPrompt) + "\" }] }] }";
            String finalUrl = apiUrl + "?key=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(finalUrl)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return extractTextWithJackson(response.body());
        } catch (Exception e) { return "Connection Error."; }
    }
    private String escapeJson(String input) { return input == null ? "" : input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " "); }
    private String extractTextWithJackson(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            if (root.has("candidates")) return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            return "No response.";
        } catch (Exception e) { return "Error parsing."; }
    }
}