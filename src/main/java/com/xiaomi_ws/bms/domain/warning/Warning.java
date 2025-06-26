package com.xiaomi_ws.bms.domain.warning;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Warning {
    private Long id;
    private VehicleId vehicleId;
    private String chassisNumber;
    private BatteryType batteryType;
    private String ruleCode;
    private String ruleName;
    private Integer warningLevel;
    private String signalData;
    private String warningDesc;
    private WarningStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Warning(VehicleId vehicleId, String chassisNumber, BatteryType batteryType,
                   String ruleCode, String ruleName, Integer warningLevel,
                   String signalData, String warningDesc) {
        this.vehicleId = vehicleId;
        this.chassisNumber = chassisNumber;
        this.batteryType = batteryType;
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.warningLevel = warningLevel;
        this.signalData = signalData;
        this.warningDesc = warningDesc;
        this.status = WarningStatus.PENDING;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 私有构造函数供Repository使用
    public Warning(Long id, VehicleId vehicleId, String chassisNumber, BatteryType batteryType,
                   String ruleCode, String ruleName, Integer warningLevel,
                   String signalData, String warningDesc, WarningStatus status,
                   LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.chassisNumber = chassisNumber;
        this.batteryType = batteryType;
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.warningLevel = warningLevel;
        this.signalData = signalData;
        this.warningDesc = warningDesc;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Jackson反序列化需要无参构造方法
    public Warning() {}

    public void process() {
        this.status = WarningStatus.PROCESSED;
        this.updateTime = LocalDateTime.now();
    }

    public void ignore() {
        this.status = WarningStatus.IGNORED;
        this.updateTime = LocalDateTime.now();
    }
}