package com.xiaomi_ws.bms.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_info")
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vid", unique = true, nullable = false, length = 16)
    private String vid;

    @Column(name = "chassis_number", unique = true, nullable = false, length = 50)
    private String chassisNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "battery_type", nullable = false)
    private BatteryTypeEnum batteryType;

    @Column(name = "total_mileage", precision = 10, scale = 2)
    private BigDecimal totalMileage;

    @Column(name = "battery_health", precision = 5, scale = 2)
    private BigDecimal batteryHealth;

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

    public enum BatteryTypeEnum {
        三元电池, 铁锂电池
    }
}
