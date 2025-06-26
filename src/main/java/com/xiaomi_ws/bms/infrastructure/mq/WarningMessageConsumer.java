package com.xiaomi_ws.bms.infrastructure.mq;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningMessageConsumer {

    @Component
    @RocketMQMessageListener(
            topic = "warning-topic",
            consumerGroup = "warning-level0-consumer",
            selectorExpression = "level-0"
    )
    public static class Level0WarningConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.error("收到0级预警(最高级): {}", message);
            try {
                WarningMessageProducer.WarningMessage warning =
                        JSON.parseObject(message, WarningMessageProducer.WarningMessage.class);
                handleCriticalWarning(warning);
            } catch (Exception e) {
                log.error("处理0级预警消息失败: {}", e.getMessage(), e);
            }
        }

        private void handleCriticalWarning(WarningMessageProducer.WarningMessage warning) {
            log.error("紧急预警处理: 车辆{}, 规则{}, 等级{}",
                    warning.getVid(), warning.getRuleName(), warning.getWarningLevel());
            // 这里可以添加紧急预警的特殊处理逻辑
            // 例如：发送短信、邮件、推送等
        }
    }

    @Component
    @RocketMQMessageListener(
            topic = "warning-topic",
            consumerGroup = "warning-level1-consumer",
            selectorExpression = "level-1"
    )
    public static class Level1WarningConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.warn("收到1级预警: {}", message);
            try {
                WarningMessageProducer.WarningMessage warning =
                        JSON.parseObject(message, WarningMessageProducer.WarningMessage.class);
                handleHighWarning(warning);
            } catch (Exception e) {
                log.error("处理1级预警消息失败: {}", e.getMessage(), e);
            }
        }

        private void handleHighWarning(WarningMessageProducer.WarningMessage warning) {
            log.warn("高级预警处理: 车辆{}, 规则{}, 等级{}",
                    warning.getVid(), warning.getRuleName(), warning.getWarningLevel());
        }
    }

    @Component
    @RocketMQMessageListener(
            topic = "warning-topic",
            consumerGroup = "warning-level2-consumer",
            selectorExpression = "level-2"
    )
    public static class Level2WarningConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.warn("收到2级预警: {}", message);
            try {
                WarningMessageProducer.WarningMessage warning =
                        JSON.parseObject(message, WarningMessageProducer.WarningMessage.class);
                handleMediumWarning(warning);
            } catch (Exception e) {
                log.error("处理2级预警消息失败: {}", e.getMessage(), e);
            }
        }

        private void handleMediumWarning(WarningMessageProducer.WarningMessage warning) {
            log.warn("中级预警处理: 车辆{}, 规则{}, 等级{}",
                    warning.getVid(), warning.getRuleName(), warning.getWarningLevel());
        }
    }

    @Component
    @RocketMQMessageListener(
            topic = "warning-topic",
            consumerGroup = "warning-level3-consumer",
            selectorExpression = "level-3"
    )
    public static class Level3WarningConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.info("收到3级预警: {}", message);
            try {
                WarningMessageProducer.WarningMessage warning =
                        JSON.parseObject(message, WarningMessageProducer.WarningMessage.class);
                handleLowWarning(warning);
            } catch (Exception e) {
                log.error("处理3级预警消息失败: {}", e.getMessage(), e);
            }
        }

        private void handleLowWarning(WarningMessageProducer.WarningMessage warning) {
            log.info("低级预警处理: 车辆{}, 规则{}, 等级{}",
                    warning.getVid(), warning.getRuleName(), warning.getWarningLevel());
        }
    }

    @Component
    @RocketMQMessageListener(
            topic = "warning-topic",
            consumerGroup = "warning-level4-consumer",
            selectorExpression = "level-4"
    )
    public static class Level4WarningConsumer implements RocketMQListener<String> {
        @Override
        public void onMessage(String message) {
            log.info("收到4级预警: {}", message);
            try {
                WarningMessageProducer.WarningMessage warning =
                        JSON.parseObject(message, WarningMessageProducer.WarningMessage.class);
                handleVeryLowWarning(warning);
            } catch (Exception e) {
                log.error("处理4级预警消息失败: {}", e.getMessage(), e);
            }
        }

        private void handleVeryLowWarning(WarningMessageProducer.WarningMessage warning) {
            log.info("极低级预警处理: 车辆{}, 规则{}, 等级{}",
                    warning.getVid(), warning.getRuleName(), warning.getWarningLevel());
        }
    }
}