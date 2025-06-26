package com.xiaomi_ws.bms.infrastructure.mapper;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import com.xiaomi_ws.bms.domain.vehicle.Vehicle;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toDomain(VehicleEntity entity) {
        if (entity == null) {
            return null;
        }

        VehicleId vehicleId = new VehicleId(entity.getVid());
        BatteryType batteryType = BatteryType.of(entity.getBatteryType().name());

        return new Vehicle(
                vehicleId,
                entity.getChassisNumber(),
                batteryType,
                entity.getTotalMileage(),
                entity.getBatteryHealth(),
                entity.getCreateTime(),
                entity.getUpdateTime()
        );
    }

    public VehicleEntity toEntity(Vehicle domain) {
        if (domain == null) {
            return null;
        }

        VehicleEntity entity = new VehicleEntity();
        entity.setVid(domain.getVehicleId().getValue());
        entity.setChassisNumber(domain.getChassisNumber());
        entity.setBatteryType(VehicleEntity.BatteryTypeEnum.valueOf(domain.getBatteryType().getValue()));
        entity.setTotalMileage(domain.getTotalMileage());
        entity.setBatteryHealth(domain.getBatteryHealth());

        return entity;
    }
}
