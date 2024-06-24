package com.app.jpr.service.ci;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.service.scm.SCMServiceFactory;
import com.app.jpr.template.ThymeleafTemplateResolver;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class JenkinsCIService implements CIService {
    private final Logger logger = LoggerFactory.getLogger(JenkinsCIService.class);

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${jenkins.username}")
    private String jenkinsUsername;

    @Value("${jenkins.password}")
    private String jenkinsPassword;

    @Autowired
    ThymeleafTemplateResolver thymeleafTemplateResolver;

    @Autowired
    SCMServiceFactory scmServiceFactory;

    public String createProject(JPRCreateRequest request , String projectDir, String gitUrl) throws Exception {
        String projectName = request.getProjectName();
        logger.info("Creating Jenkins project for project: {} github url {}", projectName, gitUrl);
        addCIFilesToSCM(projectDir, gitUrl, projectName);
        return this.createJenkinsProject(projectName, gitUrl);
    }

    private String createJenkinsProject(String projectName, String gitUrl) throws Exception {
        String userPassword = jenkinsUsername + ":" + jenkinsPassword;
        String auth = new String(Base64.getEncoder().encode(userPassword.getBytes()));
        CloseableHttpClient client = HttpClients.createDefault();
        String jobConfigXml = thymeleafTemplateResolver.createContentFromTemplate(
                "jenkins-job-config.xml", Map.of("projectName", projectName, "githubURL", gitUrl));
        logger.info("Jenkins job config xml: {}", jobConfigXml);
        return callJenkinsRestAPI(projectName, gitUrl, auth, client, jobConfigXml);
    }

    private String callJenkinsRestAPI(String projectName, String gitUrl, String auth,
                                    CloseableHttpClient client, String jobConfigXml) throws Exception {
        String result =null;
        HttpPost httpPost = new HttpPost(jenkinsUrl + "/createItem?name=" + projectName);
        httpPost.setHeader("Authorization", "Basic " + auth);
        httpPost.setHeader("Content-Type", "application/xml");
        httpPost.setEntity(new StringEntity(jobConfigXml, ContentType.APPLICATION_XML));
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = jenkinsUrl + "/job/" + projectName;
            logger.info("Jenkins project created successfully: {}", result);
        }
        client.close();
        return result;
    }

    private void addCIFilesToSCM(String projectDir, String gitUrl, String projectName) throws Exception {
        thymeleafTemplateResolver.createFileFromTemplate("Jenkinsfile.txt",
                projectDir, Map.of("projectName", projectName, "githubURL", gitUrl), "Jenkinsfile");
        thymeleafTemplateResolver.createFileFromTemplate("Dockerfile.txt",
                projectDir, Map.of("projectName", projectName), "Dockerfile");
        thymeleafTemplateResolver.createFileFromTemplate("app.yaml.txt",
                projectDir, Map.of("projectName", projectName), "app.yaml");
        scmServiceFactory.getService("git").addFilesToRepository(projectDir, gitUrl,
                "Added Jenkinsfile");
    }

}
