package com.xiaomi_ws.bms.domain.signal;

import java.time.LocalDateTime;
import java.util.List;

public interface BatterySignalRepository {

    /**
     * 保存电池信号
     * @param batterySignal 电池信号对象
     */
    void save(BatterySignal batterySignal);

    /**
     * 根据ID查询电池信号
     * @param id 信号ID
     * @return 电池信号对象，如果不存在返回null
     */
    BatterySignal findById(Long id);

    /**
     * 根据车辆ID查询电池信号列表
     * @param vid 车辆ID
     * @return 电池信号列表，按创建时间倒序排列
     */
    List<BatterySignal> findByVid(String vid);

    /**
     * 根据车架号查询电池信号列表
     * @param chassisNumber 车架号
     * @return 电池信号列表，按创建时间倒序排列
     */
    List<BatterySignal> findByChassisNumber(String chassisNumber);

    /**
     * 查询最新的电池信号
     * @param limit 限制数量
     * @return 最新的电池信号列表
     */
    List<BatterySignal> findLatest(int limit);

    /**
     * 查询指定时间之后的电池信号（用于定时任务扫描）
     * @param startTime 开始时间
     * @return 电池信号列表
     */
    List<BatterySignal> findByCreateTimeAfter(LocalDateTime startTime);

    /**
     * 更新电池信号
     * @param batterySignal 电池信号对象
     */
    void update(BatterySignal batterySignal);

    /**
     * 根据ID删除电池信号
     * @param id 信号ID
     */
    void deleteById(Long id);

    /**
     * 删除指定车辆的所有电池信号
     * @param vid 车辆ID
     */
    void deleteByVid(String vid);

    /**
     * 批量保存电池信号
     * @param batterySignals 电池信号列表
     */
    void saveAll(List<BatterySignal> batterySignals);

    /**
     * 统计指定车辆的信号数量
     * @param vid 车辆ID
     * @return 信号数量
     */
    long countByVid(String vid);

    /**
     * 查询指定时间范围内的电池信号
     * @param vid 车辆ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 电池信号列表
     */
    List<BatterySignal> findByVidAndCreateTimeBetween(String vid, LocalDateTime startTime, LocalDateTime endTime);
}