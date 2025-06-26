package com.xiaomi_ws.bms.infrastructure.mapper;

import com.alibaba.fastjson.JSON;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import com.xiaomi_ws.bms.domain.warning.Warning;
import com.xiaomi_ws.bms.domain.warning.WarningStatus;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.entity.WarningEntity;
import org.springframework.stereotype.Component;

@Component
public class WarningMapper {

    public Warning toDomain(WarningEntity entity) {
        if (entity == null) {
            return null;
        }

        VehicleId vehicleId = new VehicleId(entity.getVid());
        BatteryType batteryType = BatteryType.of(entity.getBatteryType().name());
        WarningStatus status = WarningStatus.of(entity.getStatus());

        return new Warning(
                entity.getId(),
                vehicleId,
                entity.getChassisNumber(),
                batteryType,
                entity.getRuleCode(),
                entity.getRuleName(),
                entity.getWarningLevel(),
                entity.getSignalData(),
                entity.getWarningDesc(),
                status,
                entity.getCreateTime(),
                entity.getUpdateTime()
        );
    }

    public WarningEntity toEntity(Warning domain) {
        if (domain == null) {
            return null;
        }

        WarningEntity entity = new WarningEntity();
        entity.setId(domain.getId());
        entity.setVid(domain.getVehicleId().getValue());
        entity.setChassisNumber(domain.getChassisNumber());
        entity.setBatteryType(VehicleEntity.BatteryTypeEnum.valueOf(domain.getBatteryType().getValue()));
        entity.setRuleCode(domain.getRuleCode());
        entity.setRuleName(domain.getRuleName());
        entity.setWarningLevel(domain.getWarningLevel());
        entity.setSignalData(domain.getSignalData());
        entity.setWarningDesc(domain.getWarningDesc());
        entity.setStatus(domain.getStatus().getValue());

        return entity;
    }
}

