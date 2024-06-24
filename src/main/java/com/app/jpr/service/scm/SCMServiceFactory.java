package com.app.jpr.service.scm;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import org.springframework.stereotype.Service;

@Service
public class SCMServiceFactory {
    private final GitSCMService gitSCMService;

    public SCMServiceFactory(GitSCMService gitSCMService) {
        this.gitSCMService = gitSCMService;
    }

    public SCMService getService(String scmType) {
        switch (scmType) {
            case "git":
                return gitSCMService;
            default:
                throw new IllegalArgumentException("Invalid SCM type");
        }
    }
}
