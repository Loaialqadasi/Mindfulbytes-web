package com.project.Mental.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Get the absolute path to the "uploads" folder
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // 2. Fix for Windows: Append a trailing slash if missing
        if (!uploadPath.endsWith(java.io.File.separator)) {
            uploadPath += java.io.File.separator;
        }

        // 3. Register the handler
        // "file:///" is often required for absolute paths on Windows
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadPath);
    }
}