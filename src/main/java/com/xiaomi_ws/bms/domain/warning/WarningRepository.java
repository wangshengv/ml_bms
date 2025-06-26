package com.xiaomi_ws.bms.domain.warning;

import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;

import java.util.List;

public interface WarningRepository {
    Warning save(Warning warning);
    List<Warning> findByVehicleId(VehicleId vehicleId);
    List<Warning> findByWarningLevel(int level);
    List<Warning> findByStatus(WarningStatus status);
}
