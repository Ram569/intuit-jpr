package com.app.jpr.controller;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import com.app.jpr.exceptions.JPRException;
import com.app.jpr.service.JPRServiceImpl;
import com.app.jpr.service.projects.ProjectsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JPRControllerTest {
    @InjectMocks
    private JPRController jprController;
    @Mock
    private JPRServiceImpl jprService;
    @Mock
    private ProjectsService projectsService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProjectReturnsSuccessResponse() {
        JPRCreateRequest request = new JPRCreateRequest("test", "type", "git",
                "jenkins");
        JPRCreateResponse response = new JPRCreateResponse();
        response.setScmUrl("scmUrl");
        when(jprService.create(any(JPRCreateRequest.class))).thenReturn(response);
        ResponseEntity<JPRCreateResponse> result = jprController.createProject("type", "test");
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("scmUrl", result.getBody().getScmUrl());
    }
}
