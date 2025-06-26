package com.xiaomi_ws.bms.applications.service;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.entity.WarningRuleEntity;
import com.xiaomi_ws.bms.infrastructure.jpa.WarningRuleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WarningRuleCacheService {

    private final WarningRuleJpaRepository jpaRepository;

    @Cacheable(value = "warningRuleEntities", key = "#ruleCode + '_' + #batteryType.value")
    public Optional<WarningRuleEntity> findEntityByRuleCodeAndBatteryType(String ruleCode, BatteryType batteryType) {
        VehicleEntity.BatteryTypeEnum batteryTypeEnum = VehicleEntity.BatteryTypeEnum.valueOf(batteryType.getValue());
        return jpaRepository.findByRuleCodeAndBatteryTypeAndStatus(ruleCode, batteryTypeEnum, 1);
    }

    @Cacheable(value = "warningRuleEntities", key = "#batteryType.value")
    public List<WarningRuleEntity> findEntitiesByBatteryType(BatteryType batteryType) {
        VehicleEntity.BatteryTypeEnum batteryTypeEnum = VehicleEntity.BatteryTypeEnum.valueOf(batteryType.getValue());
        return jpaRepository.findByBatteryTypeAndStatus(batteryTypeEnum, 1);
    }
}
