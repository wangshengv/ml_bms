package com.xiaomi_ws.bms.applications.scheduled;

import com.xiaomi_ws.bms.applications.service.BatterySignalService;
import com.xiaomi_ws.bms.domain.rule.RuleEngine;
import com.xiaomi_ws.bms.domain.rule.WarningRule;
import com.xiaomi_ws.bms.domain.rule.WarningRuleRepository;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatterySignal;
import com.xiaomi_ws.bms.domain.vehicle.Vehicle;
import com.xiaomi_ws.bms.domain.vehicle.VehicleRepository;
import com.xiaomi_ws.bms.domain.warning.Warning;
import com.xiaomi_ws.bms.domain.warning.WarningRepository;
import com.xiaomi_ws.bms.infrastructure.entity.BatterySignalEntity;
import com.xiaomi_ws.bms.infrastructure.mq.WarningMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarningScheduledService {

    private final BatterySignalService batterySignalService;
    private final VehicleRepository vehicleRepository;
    private final WarningRuleRepository warningRuleRepository;
    private final WarningRepository warningRepository;
    private final RuleEngine ruleEngine;
    private final WarningMessageProducer messageProducer;

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void scanAndProcessWarnings() {
        log.info("开始扫描电池信号数据并处理预警");

        try {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusMinutes(1); // 扫描最近1分钟的数据

            processWarningsForTimeRange(startTime, endTime);

        } catch (Exception e) {
            log.error("定时扫描预警处理失败: {}", e.getMessage(), e);
        }
    }

    private void processWarningsForTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        for (Vehicle vehicle : vehicles) {
            try {
                processVehicleWarnings(vehicle, startTime, endTime);
            } catch (Exception e) {
                log.error("处理车辆{}预警失败: {}", vehicle.getVehicleId().getValue(), e.getMessage(), e);
            }
        }
    }

    private void processVehicleWarnings(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        // 使用领域仓储后的查询方法，按vid查找所有信号后手动过滤时间区间
        List<com.xiaomi_ws.bms.domain.signal.BatterySignal> signals = batterySignalService.getBatterySignalsByVid(vehicle.getVehicleId().getValue());
        List<com.xiaomi_ws.bms.domain.signal.BatterySignal> filteredSignals = signals.stream()
                .filter(s -> !s.getCreateTime().isBefore(startTime) && !s.getCreateTime().isAfter(endTime))
                .toList();
        if (filteredSignals.isEmpty()) {
            return;
        }
        List<WarningRule> rules = warningRuleRepository.findByBatteryType(vehicle.getBatteryType());
        for (com.xiaomi_ws.bms.domain.signal.BatterySignal signal : filteredSignals) {
            BatterySignal domainSignal = new BatterySignal(signal.getMx(), signal.getMi(), signal.getIx(), signal.getIi());

            for (WarningRule rule : rules) {
                RuleEngine.WarningResult result = ruleEngine.evaluateRule(rule, domainSignal);

                if (result.isWarning()) {
                    Warning warning = new Warning(
                            vehicle.getVehicleId(),
                            vehicle.getChassisNumber(),
                            vehicle.getBatteryType(),
                            rule.getRuleCode(),
                            rule.getRuleName(),
                            result.getWarningLevel(),
                            domainSignal.toJson(),
                            result.getDescription()
                    );

                    warningRepository.save(warning);
                    messageProducer.sendWarningMessage(warning);
                }
            }
        }
    }
}