package com.xiaomi_ws.bms.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warning_info")
public class WarningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vid", nullable = false, length = 16)
    private String vid;

    @Column(name = "chassis_number", nullable = false, length = 50)
    private String chassisNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "battery_type", nullable = false)
    private VehicleEntity.BatteryTypeEnum batteryType;

    @Column(name = "rule_code", nullable = false, length = 20)
    private String ruleCode;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "warning_level", nullable = false)
    private Integer warningLevel;

    @Column(name = "signal_data", columnDefinition = "JSON")
    private String signalData;

    @Column(name = "warning_desc", length = 500)
    private String warningDesc;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}