package com.app.jpr.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class ThymeleafTemplateResolver {
    @Autowired
    private SpringTemplateEngine templateEngine;

    public void createFileFromTemplate(String templateName, String outputFilePath, Map<String, Object> variables, String fileName) throws IOException {
        Context context = new Context();
        context.setVariables(variables);
        String content = templateEngine.process(templateName, context);
        Path filePath = Paths.get(outputFilePath, fileName);
        Files.createFile(filePath);
        Files.write(filePath, content.getBytes());
    }

    public String createContentFromTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
