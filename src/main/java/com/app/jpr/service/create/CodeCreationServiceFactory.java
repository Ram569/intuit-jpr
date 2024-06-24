package com.app.jpr.service.create;

import com.app.jpr.util.ProjectType;
import org.springframework.stereotype.Service;

@Service
public class CodeCreationServiceFactory {
    private final WebCodeCreationService projectCreationService;
    private final LibraryCodeCreationService javaLibraryCreationService;

    public CodeCreationServiceFactory(WebCodeCreationService projectCreationService,
                                      LibraryCodeCreationService javaLibraryCreationService) {
        this.projectCreationService = projectCreationService;
        this.javaLibraryCreationService = javaLibraryCreationService;
    }

    public CodeCreationService getService(String type) {
        ProjectType projectType = ProjectType.fromString(type);
        switch (projectType) {
            case JAVA_WEB_SERVICE:
                return projectCreationService;
            case JAVA_LIBRARY:
                return javaLibraryCreationService;
            default:
                throw new IllegalArgumentException("Invalid project type");
        }
    }
}
