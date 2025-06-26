package com.xiaomi_ws.bms.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WarningRequestDto {

    @Min(value = 1, message = "车架编号必须大于0")
    @JsonProperty("carId")
    private Integer carId;

    @JsonProperty("warnId")
    private Integer warnId;

    @NotBlank(message = "信号数据不能为空")
    @JsonProperty("signal")
    private String signal;
}
