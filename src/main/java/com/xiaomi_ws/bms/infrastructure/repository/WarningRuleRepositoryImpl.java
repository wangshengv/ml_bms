package com.xiaomi_ws.bms.infrastructure.repository;

import com.xiaomi_ws.bms.applications.service.WarningRuleCacheService;
import com.xiaomi_ws.bms.domain.rule.WarningRule;
import com.xiaomi_ws.bms.domain.rule.WarningRuleRepository;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.entity.WarningRuleEntity;
import com.xiaomi_ws.bms.infrastructure.jpa.WarningRuleJpaRepository;
import com.xiaomi_ws.bms.infrastructure.mapper.WarningRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WarningRuleRepositoryImpl implements WarningRuleRepository {

    private final WarningRuleJpaRepository jpaRepository;
    private final WarningRuleMapper warningRuleMapper;
    private final WarningRuleCacheService cacheService; // 注入缓存服务

    @Override
    public Optional<WarningRule> findByRuleCodeAndBatteryType(String ruleCode, BatteryType batteryType) {
        return cacheService.findEntityByRuleCodeAndBatteryType(ruleCode, batteryType)
                .map(warningRuleMapper::toDomain);
    }

    @Override
    public List<WarningRule> findByBatteryType(BatteryType batteryType) {
        return cacheService.findEntitiesByBatteryType(batteryType)
                .stream()
                .map(warningRuleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarningRule> findAll() {
        return jpaRepository.findAll().stream()
                .map(warningRuleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"warningRuleEntities"}, allEntries = true)
    public WarningRule save(WarningRule warningRule) {
        WarningRuleEntity entity = warningRuleMapper.toEntity(warningRule);
        WarningRuleEntity saved = jpaRepository.save(entity);
        return warningRuleMapper.toDomain(saved);
    }
}