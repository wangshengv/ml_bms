package com.xiaomi_ws.bms.domain.rule;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;

import java.util.List;
import java.util.Optional;

public interface WarningRuleRepository {
    Optional<WarningRule> findByRuleCodeAndBatteryType(String ruleCode, BatteryType batteryType);
    List<WarningRule> findByBatteryType(BatteryType batteryType);
    List<WarningRule> findAll();
    WarningRule save(WarningRule warningRule);
}