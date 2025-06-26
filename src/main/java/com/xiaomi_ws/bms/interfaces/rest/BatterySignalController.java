package com.xiaomi_ws.bms.interfaces.rest;

import com.xiaomi_ws.bms.applications.service.BatterySignalService;
import com.xiaomi_ws.bms.infrastructure.entity.BatterySignalEntity;
import com.xiaomi_ws.bms.interfaces.dto.ApiResponseDto;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalRequest;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/battery-signals")
@RequiredArgsConstructor
public class BatterySignalController {

    private final BatterySignalService batterySignalService;

    @PostMapping("/report")
    public ApiResponseDto<String> reportBatterySignal(@Valid @RequestBody BatterySignalRequest request) {
        batterySignalService.saveBatterySignal(request);
        return ApiResponseDto.success("电池信号上报成功");
    }

    @GetMapping("/getBatterySignals/{vid}")
    public ApiResponseDto<List<com.xiaomi_ws.bms.domain.signal.BatterySignal>> getBatterySignals(@PathVariable String vid) {
        var signals = batterySignalService.getBatterySignalsByVid(vid);
        return ApiResponseDto.success(signals);
    }

    @GetMapping("/getBatterySignalById/{id}")
    public ApiResponseDto<com.xiaomi_ws.bms.domain.signal.BatterySignal> getBatterySignal(@PathVariable Long id) {
        var signal = batterySignalService.getBatterySignalById(id);
        return ApiResponseDto.success(signal);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponseDto<String> deleteBatterySignal(@PathVariable Long id) {
        batterySignalService.deleteBatterySignal(id);
        return ApiResponseDto.success("删除成功");
    }

    @PutMapping("/update")
    public ApiResponseDto<String> updateBatterySignal(@Valid @RequestBody BatterySignalUpdateRequest request) {
        batterySignalService.updateBatterySignal(request);
        return ApiResponseDto.success("电池信号修改成功");
    }
}
