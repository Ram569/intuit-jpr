package com.app.jpr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JPRCreateResponse {
    private String projectWorkDir;
    private String scmUrl;
    private String ciUrl;
}
