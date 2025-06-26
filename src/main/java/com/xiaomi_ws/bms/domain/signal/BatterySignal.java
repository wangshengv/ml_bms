package com.xiaomi_ws.bms.domain.signal;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatterySignal {

    private Long id;
    private String vid; // varchar(16)
    private String chassisNumber; // varchar(50)
    private String signalData; // json存储为字符串
    private BigDecimal mx; // 最高电压 decimal(10,3)
    private BigDecimal mi; // 最小电压 decimal(10,3)
    private BigDecimal ix; // 最高电流 decimal(10,3)
    private BigDecimal ii; // 最小电流 decimal(10,3)
    private LocalDateTime createTime;

    public String toJson() {
        // 只序列化业务相关字段，不包含id、createTime、signalData自身
        return JSON.toJSONString(new SignalData(mx, mi, ix, ii));
    }

    // 内部类用于序列化 signalData 字段
    @Data
    @AllArgsConstructor
    private static class SignalData {
        private BigDecimal Mx;
        private BigDecimal Mi;
        private BigDecimal Ix;
        private BigDecimal Ii;
    }
}
