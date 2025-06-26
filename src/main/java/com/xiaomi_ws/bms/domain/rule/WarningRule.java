package com.xiaomi_ws.bms.domain.rule;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WarningRule {
    private String ruleCode;
    private String ruleName;
    private BatteryType batteryType;
    private RuleExpression expression;
    private boolean enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public WarningRule(String ruleCode, String ruleName, BatteryType batteryType, RuleExpression expression) {
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.batteryType = batteryType;
        this.expression = expression;
        this.enabled = true;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 私有构造函数供Repository使用
    public WarningRule(String ruleCode, String ruleName, BatteryType batteryType, RuleExpression expression,
                       boolean enabled, LocalDateTime createTime, LocalDateTime updateTime) {
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.batteryType = batteryType;
        this.expression = expression;
        this.enabled = enabled;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void enable() {
        this.enabled = true;
        this.updateTime = LocalDateTime.now();
    }

    public void disable() {
        this.enabled = false;
        this.updateTime = LocalDateTime.now();
    }

    public void updateExpression(RuleExpression expression) {
        this.expression = expression;
        this.updateTime = LocalDateTime.now();
    }
}
