package com.app.jpr.service.create;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.util.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LibraryCodeCreationService implements CodeCreationService {
    private final Logger logger = LoggerFactory.getLogger(LibraryCodeCreationService.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String create(JPRCreateRequest request) throws Exception {
        String projectName = request.getProjectName();
        String createdZipFile = createJavaLibrary(projectName);
        Path directory = Files.createTempDirectory(projectName);
        logger.info("Created temporary directory: {} for project: {}", directory, projectName);
        ZipUtils.copyFilesFromZip(createdZipFile, directory);
        return directory.toString();
    }

    private String createJavaLibrary(String projectName) {
        String path = null;
        try {
            path = resourceLoader.getResource("classpath:java-library.zip").getFile().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Java Library Path: {}", path);
        return path;
    }
}
