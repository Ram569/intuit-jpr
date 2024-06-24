package com.app.jpr.service.scm;

import com.app.jpr.dto.JPRCreateRequest;
import org.eclipse.jgit.api.errors.GitAPIException;

public interface SCMService {
    String createRepository(JPRCreateRequest request, String projectDirectory);

    void addFilesToRepository(String directoryPath, String remoteUrl, String commitMessage) throws GitAPIException;
}
