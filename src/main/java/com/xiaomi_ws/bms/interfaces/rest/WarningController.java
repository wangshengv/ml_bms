package com.xiaomi_ws.bms.interfaces.rest;

import com.xiaomi_ws.bms.applications.command.ProcessWarningCommand;
import com.xiaomi_ws.bms.applications.query.GetVehicleWarningsQuery;
import com.xiaomi_ws.bms.applications.service.WarningApplicationService;
import com.xiaomi_ws.bms.domain.warning.Warning;
import com.xiaomi_ws.bms.interfaces.dto.ApiResponseDto;
import com.xiaomi_ws.bms.interfaces.dto.WarningRequestDto;
import com.xiaomi_ws.bms.interfaces.dto.WarningResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class WarningController {

    private final WarningApplicationService warningApplicationService;

    @PostMapping("/warn")
    public ApiResponseDto<List<WarningResponseDto>> processWarning(
            @RequestBody @Valid List<WarningRequestDto> requests) {
        try {
            if (requests == null || requests.isEmpty()) {
                return ApiResponseDto.error("请求列表不能为空");
            }

            ProcessWarningCommand command = new ProcessWarningCommand(
                    requests.stream()
                            .map(dto -> new ProcessWarningCommand.WarningRequestItem(
                                    dto.getCarId(), dto.getWarnId(), dto.getSignal()))
                            .collect(Collectors.toList())
            );

            List<WarningResponseDto> responses = warningApplicationService.processWarnings(command);
            return ApiResponseDto.success(responses);
        } catch (Exception e) {
            log.error("预警处理失败: {}", e.getMessage(), e);
            return ApiResponseDto.error("预警处理失败: " + e.getMessage());
        }
    }

    @GetMapping("/warnings/{vehicleId}")
    public ApiResponseDto<List<Warning>> getVehicleWarnings(
            @PathVariable
            @NotBlank(message = "车辆ID不能为空")
            @Size(min = 16, max = 16, message = "车辆ID必须为16位")
            String vehicleId) {
        try {
            GetVehicleWarningsQuery query = new GetVehicleWarningsQuery(vehicleId);
            List<Warning> warnings = warningApplicationService.getVehicleWarnings(query);
            return ApiResponseDto.success(warnings);
        } catch (Exception e) {
            log.error("获取预警信息失败: {}", e.getMessage(), e);
            return ApiResponseDto.error("获取预警信息失败: " + e.getMessage());
        }
    }
}
