package com.app.jpr.service.ci;

import com.app.jpr.dto.JPRCreateRequest;

public interface CIService {
    String createProject(JPRCreateRequest request, String projectDir, String gitUrl) throws Exception;
}
