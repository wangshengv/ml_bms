package com.xiaomi_ws.bms.domain.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class VehicleId {
    private final String value;

    @JsonCreator
    public VehicleId(@JsonProperty("value") String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("车辆ID不能为空");
        }
        if (value.length() != 16) {
            throw new IllegalArgumentException("车辆ID必须为16位");
        }
        this.value = value;
    }
}
