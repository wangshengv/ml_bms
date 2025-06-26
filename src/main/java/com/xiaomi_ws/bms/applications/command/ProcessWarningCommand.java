package com.xiaomi_ws.bms.applications.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
@AllArgsConstructor
public class ProcessWarningCommand {
    private List<WarningRequestItem> requests;

    @Data
    @AllArgsConstructor
    public static class WarningRequestItem {
        private Integer carId;
        private Integer warnId;
        private String signal;
    }
}
