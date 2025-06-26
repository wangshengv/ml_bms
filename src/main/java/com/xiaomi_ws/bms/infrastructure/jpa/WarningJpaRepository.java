package com.xiaomi_ws.bms.infrastructure.jpa;

import com.xiaomi_ws.bms.infrastructure.entity.WarningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningJpaRepository extends JpaRepository<WarningEntity, Long> {
    List<WarningEntity> findByVidOrderByCreateTimeDesc(String vid);
    List<WarningEntity> findByWarningLevelOrderByCreateTimeDesc(Integer warningLevel);
    List<WarningEntity> findByStatusOrderByCreateTimeDesc(Integer status);
}
