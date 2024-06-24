package com.app.jpr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JPRCreateRequest {
    private String projectName;
    private String projectType;
    private String scmType;
    private String ciType;
}
