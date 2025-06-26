package com.xiaomi_ws.bms.domain.vehicle;

import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findByVehicleId(VehicleId vehicleId);
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    List<Vehicle> findAll();
    Vehicle save(Vehicle vehicle);
}