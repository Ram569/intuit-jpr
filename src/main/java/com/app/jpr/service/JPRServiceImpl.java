package com.app.jpr.service;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import com.app.jpr.exceptions.JPRException;
import com.app.jpr.model.Projects;
import com.app.jpr.service.ci.CIService;
import com.app.jpr.service.ci.CIServiceFactory;
import com.app.jpr.service.create.CodeCreationService;
import com.app.jpr.service.create.CodeCreationServiceFactory;
import com.app.jpr.service.scm.SCMService;
import com.app.jpr.service.scm.SCMServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JPRServiceImpl implements JPRService {
    private final Logger logger = LoggerFactory.getLogger(JPRServiceImpl.class);
    private final CodeCreationServiceFactory projectCreationServiceFactory;
    private final SCMServiceFactory scmServiceFactory;
    private final CIServiceFactory ciServiceFactory;

    JPRServiceImpl(CodeCreationServiceFactory projectCreationServiceFactory,
                   SCMServiceFactory scmServiceFactory,
                   CIServiceFactory ciServiceFactory) {
        this.projectCreationServiceFactory = projectCreationServiceFactory;
        this.scmServiceFactory = scmServiceFactory;
        this.ciServiceFactory = ciServiceFactory;
    }

    @Override
    public JPRCreateResponse create(JPRCreateRequest projectCreationRequest) throws JPRException {
        JPRCreateResponse projectCreationResponse = new JPRCreateResponse();
        String createdFilePath = createCode(projectCreationRequest);
        projectCreationResponse.setProjectWorkDir(createdFilePath);
        String scmURL = createRepository(projectCreationRequest, createdFilePath);
        projectCreationResponse.setScmUrl(scmURL);
        projectCreationResponse.setCiUrl(createCIPipeLine(projectCreationRequest, createdFilePath, scmURL));
        return projectCreationResponse;
    }

    private String createCode(JPRCreateRequest projectCreationRequest) {
        try {
            CodeCreationService projectCreationService = projectCreationServiceFactory.
                    getService(projectCreationRequest.getProjectType());
            return projectCreationService.create(projectCreationRequest);
        } catch (Exception e) {
            logger.error("Exception occurred while creating code ", e);
            throw new JPRException("Exception occurred while creating code ");
        }
    }

    private String createRepository(JPRCreateRequest projectCreationRequest, String createdFilePath) {
        try {
            SCMService scmService = scmServiceFactory.getService(projectCreationRequest.getScmType());
            return scmService.createRepository(projectCreationRequest, createdFilePath);
        } catch (Exception e) {
            logger.error("Exception occurred while creating repository ", e);
            throw new JPRException("Exception occurred while creating repository ");
        }
    }

    private String createCIPipeLine(JPRCreateRequest projectCreationRequest, String createdFilePath, String scmURL) {
        try {
            CIService ciService = ciServiceFactory.getService(projectCreationRequest.getCiType());
            return ciService.createProject(projectCreationRequest, createdFilePath, scmURL);
        } catch (Exception e) {
            logger.error("Exception occurred while creating ci pipeline ", e);
            throw new JPRException("Exception occurred while creating ci pipeline ");
        }
    }
}
