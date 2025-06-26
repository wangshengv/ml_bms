package com.xiaomi_ws.bms.infrastructure.repository;

import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import com.xiaomi_ws.bms.domain.warning.Warning;
import com.xiaomi_ws.bms.domain.warning.WarningRepository;
import com.xiaomi_ws.bms.domain.warning.WarningStatus;
import com.xiaomi_ws.bms.infrastructure.entity.WarningEntity;
import com.xiaomi_ws.bms.infrastructure.jpa.WarningJpaRepository;
import com.xiaomi_ws.bms.infrastructure.mapper.WarningMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WarningRepositoryImpl implements WarningRepository {

    private final WarningJpaRepository jpaRepository;
    private final WarningMapper warningMapper;

    @Override
    @CacheEvict(value = "vehicleWarnings", key = "#warning.vehicleId.value")
    public Warning save(Warning warning) {
        WarningEntity entity = warningMapper.toEntity(warning);
        WarningEntity saved = jpaRepository.save(entity);
        return warningMapper.toDomain(saved);
    }

    @Override
    @Cacheable(value = "vehicleWarnings", key = "#vehicleId.value")
    public List<Warning> findByVehicleId(VehicleId vehicleId) {
        return jpaRepository.findByVidOrderByCreateTimeDesc(vehicleId.getValue())
                .stream()
                .map(warningMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Warning> findByWarningLevel(int level) {
        return jpaRepository.findByWarningLevelOrderByCreateTimeDesc(level)
                .stream()
                .map(warningMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Warning> findByStatus(WarningStatus status) {
        return jpaRepository.findByStatusOrderByCreateTimeDesc(status.getValue())
                .stream()
                .map(warningMapper::toDomain)
                .collect(Collectors.toList());
    }
}
