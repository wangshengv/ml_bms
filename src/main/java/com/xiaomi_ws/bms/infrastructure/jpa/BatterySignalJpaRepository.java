package com.xiaomi_ws.bms.infrastructure.jpa;

import com.xiaomi_ws.bms.infrastructure.entity.BatterySignalEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BatterySignalJpaRepository extends JpaRepository<BatterySignalEntity, Long> {

    /**
     * 根据车辆ID查询，按创建时间倒序
     */
    List<BatterySignalEntity> findByVidOrderByCreateTimeDesc(String vid);

    /**
     * 根据车架号查询，按创建时间倒序
     */
    List<BatterySignalEntity> findByChassisNumberOrderByCreateTimeDesc(String chassisNumber);

    /**
     * 查询最新记录
     */
    @Query("SELECT b FROM BatterySignalEntity b ORDER BY b.createTime DESC")
    List<BatterySignalEntity> findLatestSignals(Pageable pageable);

    /**
     * 查询指定时间之后的记录
     */
    List<BatterySignalEntity> findByCreateTimeAfterOrderByCreateTimeDesc(LocalDateTime createTime);

    /**
     * 删除指定车辆的所有记录
     */
    @Modifying
    void deleteByVid(String vid);

    /**
     * 统计指定车辆的记录数量
     */
    long countByVid(String vid);

    /**
     * 查询指定时间范围内的记录
     */
    List<BatterySignalEntity> findByVidAndCreateTimeBetweenOrderByCreateTimeDesc(
            String vid, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 批量插入（如果需要的话，可以使用原生SQL）
     */
    @Query(value = "INSERT INTO battery_signal (vid, chassis_number, signal_data, mx, mi, ix, ii) VALUES " +
            "(:#{#entity.vid}, :#{#entity.chassisNumber}, :#{#entity.signalData}, " +
            ":#{#entity.mx}, :#{#entity.mi}, :#{#entity.ix}, :#{#entity.ii})",
            nativeQuery = true)
    @Modifying
    void insertBatch(@Param("entity") BatterySignalEntity entity);
}
