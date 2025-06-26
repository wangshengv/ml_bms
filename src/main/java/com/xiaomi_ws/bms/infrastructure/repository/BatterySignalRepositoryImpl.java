package com.xiaomi_ws.bms.infrastructure.repository;

import com.xiaomi_ws.bms.domain.signal.BatterySignal;
import com.xiaomi_ws.bms.domain.signal.BatterySignalRepository;
import com.xiaomi_ws.bms.infrastructure.entity.BatterySignalEntity;
import com.xiaomi_ws.bms.infrastructure.jpa.BatterySignalJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BatterySignalRepositoryImpl implements BatterySignalRepository {

    private final BatterySignalJpaRepository jpaRepository;

    @Override
    @CacheEvict(value = "battery-signals", key = "#batterySignal.vid")
    public void save(BatterySignal batterySignal) {
        log.debug("保存电池信号: vid={}", batterySignal.getVid());
        BatterySignalEntity entity = convertToEntity(batterySignal);
        BatterySignalEntity saved = jpaRepository.save(entity);
        batterySignal.setId(saved.getId());
        batterySignal.setCreateTime(saved.getCreateTime());
    }

    @Override
    public BatterySignal findById(Long id) {
        log.debug("查询电池信号: id={}", id);
        return jpaRepository.findById(id)
                .map(this::convertToDomain)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "battery-signals", key = "#vid")
    public List<BatterySignal> findByVid(String vid) {
        log.debug("查询车辆电池信号: vid={}", vid);
        return jpaRepository.findByVidOrderByCreateTimeDesc(vid)
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatterySignal> findByChassisNumber(String chassisNumber) {
        log.debug("查询车架号电池信号: chassisNumber={}", chassisNumber);
        return jpaRepository.findByChassisNumberOrderByCreateTimeDesc(chassisNumber)
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatterySignal> findLatest(int limit) {
        log.debug("查询最新电池信号: limit={}", limit);
        return jpaRepository.findLatestSignals(PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatterySignal> findByCreateTimeAfter(LocalDateTime startTime) {
        log.debug("查询时间范围电池信号: startTime={}", startTime);
        return jpaRepository.findByCreateTimeAfterOrderByCreateTimeDesc(startTime)
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "battery-signals", key = "#batterySignal.vid")
    public void update(BatterySignal batterySignal) {
        log.debug("更新电池信号: id={}", batterySignal.getId());
        BatterySignalEntity entity = convertToEntity(batterySignal);
        jpaRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("删除电池信号: id={}", id);
        BatterySignal signal = findById(id);
        if (signal != null) {
            // 直接调用带缓存清除的方法
            deleteByIdWithCacheClear(id, signal.getVid());
        }
    }

    // 改为public方法
    @CacheEvict(value = "battery-signals", key = "#vid")
    public void deleteByIdWithCacheClear(Long id, String vid) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "battery-signals", key = "#vid")
    public void deleteByVid(String vid) {
        log.debug("删除车辆所有电池信号: vid={}", vid);
        jpaRepository.deleteByVid(vid);
    }

    @Override
    @Transactional
    public void saveAll(List<BatterySignal> batterySignals) {
        log.debug("批量保存电池信号: count={}", batterySignals.size());
        List<BatterySignalEntity> entities = batterySignals.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);

        // 清除相关缓存
        batterySignals.forEach(signal ->
                clearCacheByVid(signal.getVid())
        );
    }

    // 改为public方法
    @CacheEvict(value = "battery-signals", key = "#vid")
    public void clearCacheByVid(String vid) {
        log.debug("清除缓存: vid={}", vid);
        // 缓存清除方法，方法体可以为空，注解会处理缓存清除
    }

    @Override
    public long countByVid(String vid) {
        log.debug("统计车辆信号数量: vid={}", vid);
        return jpaRepository.countByVid(vid);
    }

    @Override
    public List<BatterySignal> findByVidAndCreateTimeBetween(String vid, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("查询时间范围内车辆信号: vid={}, startTime={}, endTime={}", vid, startTime, endTime);
        return jpaRepository.findByVidAndCreateTimeBetweenOrderByCreateTimeDesc(vid, startTime, endTime)
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    // 转换方法保持private
    private BatterySignalEntity convertToEntity(BatterySignal domain) {
        BatterySignalEntity entity = new BatterySignalEntity();
        entity.setId(domain.getId());
        entity.setVid(domain.getVid());
        entity.setChassisNumber(domain.getChassisNumber());
        // 保证 signal_data 字段不为 null
        entity.setSignalData(domain.toJson());
        entity.setMx(domain.getMx());
        entity.setMi(domain.getMi());
        entity.setIx(domain.getIx());
        entity.setIi(domain.getIi());
        entity.setCreateTime(domain.getCreateTime());
        return entity;
    }

    private BatterySignal convertToDomain(BatterySignalEntity entity) {
        return BatterySignal.builder()
                .id(entity.getId())
                .vid(entity.getVid())
                .chassisNumber(entity.getChassisNumber())
                .signalData(entity.getSignalData())
                .mx(entity.getMx())
                .mi(entity.getMi())
                .ix(entity.getIx())
                .ii(entity.getIi())
                .createTime(entity.getCreateTime())
                .build();
    }
}
