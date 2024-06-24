package com.app.jpr.service.scm;

import com.app.jpr.dto.JPRCreateRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitSCMService implements SCMService {
    private final Logger logger = LoggerFactory.getLogger(GitSCMService.class);
    @Value("${github.username}")
    private String USER_NAME;
    @Value("${github.password}")
    private String PASSWORD;

    @Override
    public String createRepository(JPRCreateRequest request, String directoryPath) {
        try {
            String projectName = request.getProjectName();
            String createdRepoUrl = createRemoteRepository(projectName);
            logger.info("Remote repository {} created for project: {}", createdRepoUrl, projectName);
            addFilesToRepository(directoryPath, createdRepoUrl, "Initial commit");
            logger.info("Repository created and pushed to GitHub for project: {}", projectName);
            return createdRepoUrl;
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            return "Error creating repository";
        }
    }


    private String createRemoteRepository(String projectName) throws IOException {
        RepositoryService service = new RepositoryService();
        service.getClient().setCredentials(USER_NAME, PASSWORD);
        Repository newRepository = new Repository();
        newRepository.setName(projectName);
        newRepository = service.createRepository(newRepository);
        return newRepository.getCloneUrl();
    }

    @Override
    public void addFilesToRepository(String directoryPath, String remoteUrl, String commitMessage) throws GitAPIException {
        Git git = Git.init().setDirectory(new File(directoryPath)).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage(commitMessage).call();
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(USER_NAME, PASSWORD);
        git.push()
                .setRemote(remoteUrl)
                .setCredentialsProvider(credentialsProvider)
                .call();
    }

}