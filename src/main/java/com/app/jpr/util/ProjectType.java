package com.app.jpr.util;

public enum ProjectType {
    JAVA_LIBRARY("JavaLibrary"),
    JAVA_WEB_SERVICE("JavaWebService");

    private String projectType;
    ProjectType(String projectType) {
        this.projectType = projectType;
    }
    public String getProjectType() {
        return projectType;
    }
    public static ProjectType fromString(String projectType) {
        for (ProjectType type : ProjectType.values()) {
            if (type.getProjectType().equalsIgnoreCase(projectType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum found with projectType: " + projectType);
    }
}
