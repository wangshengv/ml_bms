package com.xiaomi_ws.bms.domain.vehicle;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Vehicle {
    private VehicleId vehicleId;
    private String chassisNumber;
    private BatteryType batteryType;
    private BigDecimal totalMileage;
    private BigDecimal batteryHealth;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Vehicle(VehicleId vehicleId, String chassisNumber, BatteryType batteryType) {
        this.vehicleId = vehicleId;
        this.chassisNumber = chassisNumber;
        this.batteryType = batteryType;
        this.totalMileage = BigDecimal.ZERO;
        this.batteryHealth = new BigDecimal("100.00");
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public Vehicle(VehicleId vehicleId, String chassisNumber, BatteryType batteryType,
                   BigDecimal totalMileage, BigDecimal batteryHealth) {
        this.vehicleId = vehicleId;
        this.chassisNumber = chassisNumber;
        this.batteryType = batteryType;
        this.totalMileage = totalMileage;
        this.batteryHealth = batteryHealth;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 私有构造函数供Repository使用
    public Vehicle(VehicleId vehicleId, String chassisNumber, BatteryType batteryType,
                   BigDecimal totalMileage, BigDecimal batteryHealth,
                   LocalDateTime createTime, LocalDateTime updateTime) {
        this.vehicleId = vehicleId;
        this.chassisNumber = chassisNumber;
        this.batteryType = batteryType;
        this.totalMileage = totalMileage;
        this.batteryHealth = batteryHealth;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void updateMileage(BigDecimal mileage) {
        this.totalMileage = mileage;
        this.updateTime = LocalDateTime.now();
    }

    public void updateBatteryHealth(BigDecimal health) {
        this.batteryHealth = health;
        this.updateTime = LocalDateTime.now();
    }
    public Vehicle() {
        // 必须的无参构造函数，Jackson 反序列化时用
    }
}