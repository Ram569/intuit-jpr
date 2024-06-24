package com.app.jpr.controller;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import com.app.jpr.model.Projects;
import com.app.jpr.service.JPRServiceImpl;
import com.app.jpr.service.projects.ProjectsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "jpr")
public class JPRController {

    private final JPRServiceImpl jPRServiceImpl;
    private final ProjectsService projectsService;

    public JPRController(JPRServiceImpl jPRServiceImpl, ProjectsService projectsService) {
        this.jPRServiceImpl = jPRServiceImpl;
        this.projectsService = projectsService;
    }

    @PostMapping("/v1/create/{type}/{name}")
    public ResponseEntity<JPRCreateResponse> createProject(@PathVariable String type, @PathVariable String name) {
        JPRCreateRequest projectCreationRequest = new JPRCreateRequest(name, type, "git", "jenkins");
        JPRCreateResponse projectCreationResponse = jPRServiceImpl.create(projectCreationRequest);
        createProject(name, projectCreationResponse);
        return ResponseEntity.ok(projectCreationResponse);
    }

    private void createProject(String name, JPRCreateResponse projectCreationResponse) {
        Projects projects = new Projects();
        projects.setCreatedBy("admin");
        projects.setName(name);
        projects.setScmURL(projectCreationResponse.getScmUrl());
        projects.setCiURL(projectCreationResponse.getScmUrl());
        projects.setCreateDate(new Date());
        projectsService.createProject(projects);
    }
}
