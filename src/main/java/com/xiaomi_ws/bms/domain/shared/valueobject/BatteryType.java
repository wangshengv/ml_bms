package com.xiaomi_ws.bms.domain.shared.valueobject;

import lombok.Getter;

@Getter
public enum BatteryType {
    TERNARY("三元电池"),
    LFP("铁锂电池");

    private final String value;

    BatteryType(String value) {
        this.value = value;
    }

    public static BatteryType of(String value) {
        for (BatteryType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的电池类型: " + value);
    }
}
