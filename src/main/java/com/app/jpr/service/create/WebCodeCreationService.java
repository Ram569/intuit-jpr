package com.app.jpr.service.create;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.exceptions.JPRException;
import com.app.jpr.template.ThymeleafTemplateResolver;
import com.app.jpr.util.ZipUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebCodeCreationService implements CodeCreationService {
    private final Logger logger = LoggerFactory.getLogger(WebCodeCreationService.class);
    @Value("${spring.initializer.url}")
    private String SPRING_API_URL;

    @Autowired
    ThymeleafTemplateResolver thymeleafTemplateResolver;

    @Override
    public String create(JPRCreateRequest request) throws Exception {
        return createJavaWebService(request.getProjectName());
    }

    private String createJavaWebService(String projectName) throws Exception {
        Path projectTempDirectory = Files.createTempDirectory(projectName);
        Map<String, String> queryParams = getQueryParameters(projectName);
        String createdZipFile = createSpringBootProject(queryParams, projectName + ".zip",
                projectTempDirectory.toString());
        logger.info("Created project work directory: {} for project: {}", projectTempDirectory, projectName);
        ZipUtils.copyFilesFromZip(createdZipFile, projectTempDirectory);
        String projectWorkDirectory = projectTempDirectory.toString() + "/" + projectName;
        addController(projectWorkDirectory, queryParams.get("packageName"));
        return projectWorkDirectory;
    }

    private Map<String, String> getQueryParameters(String projectName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("type", "gradle-project");
        queryParams.put("language", "java");
        queryParams.put("bootVersion", "3.3.0");
        queryParams.put("baseDir", projectName);
        queryParams.put("groupId", "com.app");
        queryParams.put("artifactId", projectName);
        queryParams.put("name", projectName);
        queryParams.put("description", projectName);
        queryParams.put("packageName", "com.app." + projectName);
        queryParams.put("packaging", "jar");
        queryParams.put("javaVersion", "17");
        queryParams.put("dependencies", "web");
        return queryParams;
    }

    private String createSpringBootProject(Map<String, String> queryParams, String zipFileName,
                                           String projectDirectory) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = buildURIWithParams(SPRING_API_URL, queryParams);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            Path zipFile = Paths.get(projectDirectory + "/" + zipFileName);
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(zipFile,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            if (response.statusCode() == 200) {
                logger.info("zip file downloaded successfully in the directory: {}", projectDirectory);
                return zipFile.toString();
            } else {
                logger.error("Error occurred while creating a project" + response.statusCode());
                throw new JPRException("Error occurred while creating a project" + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating a project", e);
            throw new JPRException("Error occurred while creating a project");
        }
    }

    private URI buildURIWithParams(String baseURI, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(baseURI);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        return uriBuilder.build();
    }

    private void addController(String projectWorkDirectory, String packageName) throws Exception {
        String controllerDirectory = projectWorkDirectory + "/src/main/java/" + packageName.replace(".", "/");
        Path controllerPath = Paths.get(controllerDirectory);
        Files.createDirectories(controllerPath);
        thymeleafTemplateResolver.createFileFromTemplate("WelcomeController.java.txt",
                controllerPath.toString(), Map.of("packageName", packageName), "WelcomeController.java");
    }
}
