package com.app.jpr.service.create;

import com.app.jpr.dto.JPRCreateRequest;

public interface CodeCreationService {
    String create(JPRCreateRequest request) throws Exception;
}
