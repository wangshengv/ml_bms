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
@Table(name = "battery_signal")
public class BatterySignalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vid", nullable = false, length = 16)
    private String vid;

    @Column(name = "chassis_number", nullable = false, length = 50)
    private String chassisNumber;

    @Column(name = "signal_data", columnDefinition = "JSON")
    private String signalData;

    @Column(name = "mx", precision = 10, scale = 3)
    private BigDecimal mx;

    @Column(name = "mi", precision = 10, scale = 3)
    private BigDecimal mi;

    @Column(name = "ix", precision = 10, scale = 3)
    private BigDecimal ix;

    @Column(name = "ii", precision = 10, scale = 3)
    private BigDecimal ii;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
