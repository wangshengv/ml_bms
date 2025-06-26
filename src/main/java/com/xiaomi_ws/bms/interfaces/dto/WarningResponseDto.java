package com.xiaomi_ws.bms.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WarningResponseDto {

    @JsonProperty("车架编号")
    private String chassisNumber;

    @JsonProperty("电池类型")
    private String batteryType;

    @JsonProperty("warnName")
    private String warnName;

    @JsonProperty("warnLevel")
    private Integer warnLevel;

    private String message;
}
