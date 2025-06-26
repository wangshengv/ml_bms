package com.xiaomi_ws.bms.domain.shared.valueobject;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class BatterySignal {
    private BigDecimal mx; // 最高电压
    private BigDecimal mi; // 最小电压
    private BigDecimal ix; // 最高电流
    private BigDecimal ii; // 最小电流

    public BatterySignal(String signalJson) {
        parseFromJson(signalJson);
    }

    public BatterySignal(BigDecimal mx, BigDecimal mi, BigDecimal ix, BigDecimal ii) {
        this.mx = mx;
        this.mi = mi;
        this.ix = ix;
        this.ii = ii;
    }

    private void parseFromJson(String signalJson) {
        try {
            Map<String, Object> signalMap = JSON.parseObject(signalJson, Map.class);
            this.mx = signalMap.containsKey("Mx") ? new BigDecimal(signalMap.get("Mx").toString()) : null;
            this.mi = signalMap.containsKey("Mi") ? new BigDecimal(signalMap.get("Mi").toString()) : null;
            this.ix = signalMap.containsKey("Ix") ? new BigDecimal(signalMap.get("Ix").toString()) : null;
            this.ii = signalMap.containsKey("Ii") ? new BigDecimal(signalMap.get("Ii").toString()) : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid signal data format: " + signalJson, e);
        }
    }

    public BigDecimal getVoltageDiff() {
        if (mx == null || mi == null) {
            return BigDecimal.ZERO;
        }
        return mx.subtract(mi);
    }

    public BigDecimal getCurrentDiff() {
        if (ix == null || ii == null) {
            return BigDecimal.ZERO;
        }
        return ix.subtract(ii);
    }

    public boolean hasVoltageData() {
        return mx != null && mi != null;
    }

    public boolean hasCurrentData() {
        return ix != null && ii != null;
    }

    public String toJson() {
        Map<String, Object> map = new java.util.HashMap<>();
        if (mx != null) map.put("Mx", mx);
        if (mi != null) map.put("Mi", mi);
        if (ix != null) map.put("Ix", ix);
        if (ii != null) map.put("Ii", ii);
        return JSON.toJSONString(map);
    }
}
