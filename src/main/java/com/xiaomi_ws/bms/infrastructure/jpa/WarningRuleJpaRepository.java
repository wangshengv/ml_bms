package com.xiaomi_ws.bms.infrastructure.jpa;

import com.xiaomi_ws.bms.domain.rule.WarningRule;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.entity.WarningRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarningRuleJpaRepository extends JpaRepository<WarningRuleEntity, Long> {
    Optional<WarningRuleEntity> findByRuleCodeAndBatteryTypeAndStatus(String ruleCode, VehicleEntity.BatteryTypeEnum batteryType, Integer status);
    List<WarningRuleEntity> findByBatteryTypeAndStatus(VehicleEntity.BatteryTypeEnum batteryType, Integer status);
}

