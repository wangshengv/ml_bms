package com.xiaomi_ws.bms.infrastructure.repository;

import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import com.xiaomi_ws.bms.domain.vehicle.Vehicle;
import com.xiaomi_ws.bms.domain.vehicle.VehicleRepository;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.jpa.VehicleJpaRepository;
import com.xiaomi_ws.bms.infrastructure.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleJpaRepository jpaRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Cacheable(value = "vehicles", key = "#vehicleId.value")
    public Optional<Vehicle> findByVehicleId(VehicleId vehicleId) {
        return jpaRepository.findByVid(vehicleId.getValue())
                .map(vehicleMapper::toDomain);
    }

    @Override
    @Cacheable(value = "vehicles", key = "#chassisNumber")
    public Optional<Vehicle> findByChassisNumber(String chassisNumber) {
        return jpaRepository.findByChassisNumber(chassisNumber)
                .map(vehicleMapper::toDomain);
    }

    @Override
    public List<Vehicle> findAll() {
        return jpaRepository.findAll().stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = vehicleMapper.toEntity(vehicle);
        VehicleEntity saved = jpaRepository.save(entity);
        return vehicleMapper.toDomain(saved);
    }
}
