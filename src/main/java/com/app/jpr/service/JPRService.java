package com.app.jpr.service;

import com.app.jpr.dto.JPRCreateRequest;
import com.app.jpr.dto.JPRCreateResponse;
import com.app.jpr.exceptions.JPRException;

public interface JPRService {
    JPRCreateResponse create(JPRCreateRequest createRequest) throws JPRException;
}
