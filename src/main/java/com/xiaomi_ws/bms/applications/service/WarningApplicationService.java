package com.xiaomi_ws.bms.applications.service;

import com.xiaomi_ws.bms.applications.command.ProcessWarningCommand;
import com.xiaomi_ws.bms.applications.query.GetVehicleWarningsQuery;
import com.xiaomi_ws.bms.domain.rule.RuleEngine;
import com.xiaomi_ws.bms.domain.rule.WarningRule;
import com.xiaomi_ws.bms.domain.rule.WarningRuleRepository;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatterySignal;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.domain.shared.valueobject.VehicleId;
import com.xiaomi_ws.bms.domain.vehicle.Vehicle;
import com.xiaomi_ws.bms.domain.vehicle.VehicleRepository;
import com.xiaomi_ws.bms.domain.warning.Warning;
import com.xiaomi_ws.bms.domain.warning.WarningRepository;
import com.xiaomi_ws.bms.infrastructure.mq.WarningMessageProducer;
import com.xiaomi_ws.bms.interfaces.dto.WarningResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarningApplicationService {

    private final VehicleRepository vehicleRepository;
    private final WarningRuleRepository warningRuleRepository;
    private final WarningRepository warningRepository;
    private final RuleEngine ruleEngine;
    private final WarningMessageProducer messageProducer;
    private final BatterySignalService batterySignalService;

    @Transactional
    public List<WarningResponseDto> processWarnings(ProcessWarningCommand command) {
        List<WarningResponseDto> results = new ArrayList<>();

        for (ProcessWarningCommand.WarningRequestItem item : command.getRequests()) {
            try {
                List<WarningResponseDto> itemResults = processSingleWarning(item);
                results.addAll(itemResults);
            } catch (Exception e) {
                log.error("处理预警请求失败, carId: {}, error: {}", item.getCarId(), e.getMessage(), e);
                WarningResponseDto errorResult = new WarningResponseDto();
                errorResult.setChassisNumber(item.getCarId().toString());
                errorResult.setMessage("处理失败: " + e.getMessage());
                results.add(errorResult);
            }
        }

        return results;
    }

    private List<WarningResponseDto> processSingleWarning(ProcessWarningCommand.WarningRequestItem item) {
        // 1. 根据车架编号查找车辆信息
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByChassisNumber(item.getCarId().toString());
        if (!vehicleOpt.isPresent()) {
            throw new IllegalArgumentException("车辆不存在，车架编号: " + item.getCarId());
        }

        Vehicle vehicle = vehicleOpt.get();
        BatterySignal signal = new BatterySignal(item.getSignal());

        // 2. 保存电池信号数据
        com.xiaomi_ws.bms.interfaces.dto.BatterySignalRequest signalRequest = new com.xiaomi_ws.bms.interfaces.dto.BatterySignalRequest(
            vehicle.getVehicleId().getValue(),
            vehicle.getChassisNumber(),
            signal.getMx(),
            signal.getMi(),
            signal.getIx(),
            signal.getIi()
        );
        batterySignalService.saveBatterySignal(signalRequest);

        // 3. 获取需要评估的规则
        List<WarningRule> rulesToEvaluate = getRulesToEvaluate(item.getWarnId(), vehicle.getBatteryType());

        // 4. 评估规则并生成预警
        List<WarningResponseDto> warnings = new ArrayList<>();
        for (WarningRule rule : rulesToEvaluate) {
            RuleEngine.WarningResult result = ruleEngine.evaluateRule(rule, signal);

            if (result.isWarning()) {
                // 创建预警记录
                Warning warning = new Warning(
                        vehicle.getVehicleId(),
                        vehicle.getChassisNumber(),
                        vehicle.getBatteryType(),
                        rule.getRuleCode(),
                        rule.getRuleName(),
                        result.getWarningLevel(),
                        signal.toJson(),
                        result.getDescription()
                );

                warningRepository.save(warning);

                // 发送MQ消息
                messageProducer.sendWarningMessage(warning);

                // 构建响应
                WarningResponseDto dto = new WarningResponseDto();
                dto.setChassisNumber(vehicle.getChassisNumber());
                dto.setBatteryType(vehicle.getBatteryType().getValue());
                dto.setWarnName(rule.getRuleName());
                dto.setWarnLevel(result.getWarningLevel());
                warnings.add(dto);
            }
        }

        // 如果没有预警，返回正常状态
        if (warnings.isEmpty()) {
            WarningResponseDto normalDto = new WarningResponseDto();
            normalDto.setChassisNumber(vehicle.getChassisNumber());
            normalDto.setBatteryType(vehicle.getBatteryType().getValue());
            normalDto.setMessage("正常");
            warnings.add(normalDto);
        }

        return warnings;
    }

    private List<WarningRule> getRulesToEvaluate(Integer warnId, BatteryType batteryType) {
        if (warnId != null) {
            // 评估指定规则
            Optional<WarningRule> rule = warningRuleRepository.findByRuleCodeAndBatteryType(
                    warnId.toString(), batteryType);
            return rule.map(List::of).orElse(new ArrayList<>());
        } else {
            // 评估所有规则
            return warningRuleRepository.findByBatteryType(batteryType);
        }
    }

    @Cacheable(value = "vehicleWarnings", key = "#query.vehicleId")
    public List<Warning> getVehicleWarnings(GetVehicleWarningsQuery query) {
        VehicleId vehicleId = new VehicleId(query.getVehicleId());
        return warningRepository.findByVehicleId(vehicleId);
    }
}
