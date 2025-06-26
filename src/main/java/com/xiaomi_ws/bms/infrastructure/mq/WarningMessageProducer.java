package com.xiaomi_ws.bms.infrastructure.mq;

import com.alibaba.fastjson.JSON;
import com.xiaomi_ws.bms.domain.warning.Warning;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarningMessageProducer {

    private final RocketMQTemplate rocketMQTemplate;

    public void sendWarningMessage(Warning warning) {
        try {
            WarningMessage message = new WarningMessage(
                    warning.getVehicleId().getValue(),
                    warning.getChassisNumber(),
                    warning.getBatteryType().getValue(),
                    warning.getRuleCode(),
                    warning.getRuleName(),
                    warning.getWarningLevel(),
                    warning.getSignalData(),
                    warning.getWarningDesc()
            );

            String topic = "warning-topic";
            String tag = "level-" + warning.getWarningLevel();

            // 使用 RocketMQ 发送消息
            rocketMQTemplate.convertAndSend(topic + ":" + tag, JSON.toJSONString(message));

            log.info("发送预警消息到RocketMQ: vid={}, level={}, topic={}",
                    warning.getVehicleId().getValue(), warning.getWarningLevel(), topic);
        } catch (Exception e) {
            log.error("发送预警消息失败: {}", e.getMessage(), e);
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class WarningMessage {
        private String vid;
        private String chassisNumber;
        private String batteryType;
        private String ruleCode;
        private String ruleName;
        private Integer warningLevel;
        private String signalData;
        private String warningDesc;
    }
}