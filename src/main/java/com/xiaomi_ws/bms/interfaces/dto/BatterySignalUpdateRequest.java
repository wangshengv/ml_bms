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
public class BatterySignalUpdateRequest {
    @NotNull(message = "ID不能为空")
    private Long id;

    @NotBlank(message = "车辆ID不能为空")
    @Size(min = 16, max = 16, message = "车辆ID必须为16位")
    private String vid;

    @NotBlank(message = "车架号不能为空")
    @Size(max = 50, message = "车架号长度不能超过50个字符")
    private String chassisNumber;

    private BigDecimal mx;
    private BigDecimal mi;
    private BigDecimal ix;
    private BigDecimal ii;

    public VehicleId getVehicleId() {
        return new VehicleId(vid);
    }

    public BatterySignal toBatterySignal() {
        return new BatterySignal(mx, mi, ix, ii);
    }
}
