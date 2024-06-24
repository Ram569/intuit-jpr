package com.app.jpr.service.ci;

import com.app.jpr.dto.JPRCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class CIServiceFactory {
    private final JenkinsCIService jenkinsCIService;

    public CIServiceFactory(JenkinsCIService jenkinsCIService) {
        this.jenkinsCIService = jenkinsCIService;
    }

    public CIService getService(String type) {
        switch (type) {
            case "jenkins":
                return jenkinsCIService;
            default:
                throw new IllegalArgumentException("Invalid CI type");
        }
    }
}
