package com.app.jpr.service;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import com.app.jpr.exceptions.JPRException;
import com.app.jpr.service.ci.CIService;
import com.app.jpr.service.ci.CIServiceFactory;
import com.app.jpr.service.create.CodeCreationService;
import com.app.jpr.service.create.CodeCreationServiceFactory;
import com.app.jpr.service.scm.SCMService;
import com.app.jpr.service.scm.SCMServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JPRServiceTest {
    @InjectMocks
    private JPRServiceImpl jprService;
    @Mock
    private CodeCreationServiceFactory codeCreationServiceFactory;
    @Mock
    private SCMServiceFactory scmServiceFactory;
    @Mock
    private CIServiceFactory ciServiceFactory;
    @Mock
    private CodeCreationService codeCreationService;
    @Mock
    private SCMService scmService;
    @Mock
    private CIService ciService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createReturnsSuccessResponse() throws Exception {
        JPRCreateRequest request = new JPRCreateRequest("test", "type", "git",
                "jenkins");
        when(codeCreationServiceFactory.getService(any(String.class))).thenReturn(codeCreationService);
        when(codeCreationService.create(any(JPRCreateRequest.class))).thenReturn("createdFilePath");
        when(scmServiceFactory.getService(any(String.class))).thenReturn(scmService);
        when(scmService.createRepository(any(JPRCreateRequest.class), any(String.class))).thenReturn("scmUrl");
        when(ciServiceFactory.getService(any(String.class))).thenReturn(ciService);
        when(ciService.createProject(any(JPRCreateRequest.class), any(String.class),
                any(String.class))).thenReturn("ciUrl");

        JPRCreateResponse response = jprService.create(request);

        assertEquals("createdFilePath", response.getProjectWorkDir());
        assertEquals("scmUrl", response.getScmUrl());
        assertEquals("ciUrl", response.getCiUrl());
    }

    @Test
    public void createThrowsExceptionWhenCodeCreationFails() throws Exception {
        JPRCreateRequest request = new JPRCreateRequest("test", "type",
                "git", "jenkins");
        when(codeCreationServiceFactory.getService(any(String.class))).thenReturn(codeCreationService);
        when(codeCreationService.create(any(JPRCreateRequest.class))).
                thenThrow(new JPRException("Exception occurred while creating code"));

        assertThrows(JPRException.class, () -> jprService.create(request));
    }

    @Test
    public void createThrowsExceptionWhenRepositoryCreationFails() throws Exception {
        JPRCreateRequest request = new JPRCreateRequest("test", "type",
                "git", "jenkins");
        when(codeCreationServiceFactory.getService(any(String.class))).thenReturn(codeCreationService);
        when(codeCreationService.create(any(JPRCreateRequest.class))).thenReturn("createdFilePath");
        when(scmServiceFactory.getService(any(String.class))).thenReturn(scmService);
        when(scmService.createRepository(any(JPRCreateRequest.class), any(String.class))).
                thenThrow(new JPRException("Exception occurred while creating repository"));

        assertThrows(JPRException.class, () -> jprService.create(request));
    }

    @Test
    public void createThrowsExceptionWhenCIPipelineCreationFails() throws Exception {
        JPRCreateRequest request = new JPRCreateRequest("test", "type",
                "git", "jenkins");
        when(codeCreationServiceFactory.getService(any(String.class))).thenReturn(codeCreationService);
        when(codeCreationService.create(any(JPRCreateRequest.class))).thenReturn("createdFilePath");
        when(scmServiceFactory.getService(any(String.class))).thenReturn(scmService);
        when(scmService.createRepository(any(JPRCreateRequest.class), any(String.class))).thenReturn("scmUrl");
        when(ciServiceFactory.getService(any(String.class))).thenReturn(ciService);
        when(ciService.createProject(any(JPRCreateRequest.class), any(String.class),
                any(String.class))).thenThrow(new JPRException("Exception occurred while creating ci pipeline"));

        assertThrows(JPRException.class, () -> jprService.create(request));
    }
}
