package com.xiaomi_ws.bms.domain.warning;

import lombok.Getter;

@Getter
public enum WarningStatus {
    PENDING(0, "待处理"),
    PROCESSED(1, "已处理"),
    IGNORED(2, "已忽略");

    private final int value;
    private final String description;

    WarningStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static WarningStatus of(int value) {
        for (WarningStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("不支持的预警状态: " + value);
    }
}
