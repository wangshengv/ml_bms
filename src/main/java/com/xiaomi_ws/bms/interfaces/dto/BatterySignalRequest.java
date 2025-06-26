package com.xiaomi_ws.bms.interfaces.dto;

import com.xiaomi_ws.bms.domain.shared.valueobject.BatterySignal;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatterySignalRequest {

    @NotBlank(message = "车辆ID不能为空")
    @Size(min = 16, max = 16, message = "车辆ID必须为16位")
    private String vid;

    @NotBlank(message = "车架号不能为空")
    @Size(max = 50, message = "车架号长度不能超过50个字符")
    private String chassisNumber;

    private BigDecimal mx; // 最高电压
    private BigDecimal mi; // 最小电压
    private BigDecimal ix; // 最高电流
    private BigDecimal ii; // 最小电流

    /**
     * 转换为VehicleId值对象
     */
    public VehicleId getVehicleId() {
        return new VehicleId(vid);
    }

    /**
     * 转换为BatterySignal领域对象
     */
    public BatterySignal toBatterySignal() {
        return new BatterySignal(mx, mi, ix, ii);
    }
}
