package com.xiaomi_ws.bms.infrastructure.jpa;

import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long> {
    Optional<VehicleEntity> findByVid(String vid);
    Optional<VehicleEntity> findByChassisNumber(String chassisNumber);
}
