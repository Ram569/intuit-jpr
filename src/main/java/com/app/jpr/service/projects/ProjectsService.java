package com.app.jpr.service.projects;

import com.app.jpr.model.Projects;
import com.app.jpr.repository.ProjectsRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectsService {
    private final ProjectsRepository projectsRepository;

    ProjectsService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    public void createProject(Projects project) {
        this.projectsRepository.save(project);
    }
}
