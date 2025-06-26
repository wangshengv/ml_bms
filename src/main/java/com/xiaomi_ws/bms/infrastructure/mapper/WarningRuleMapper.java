package com.xiaomi_ws.bms.infrastructure.mapper;

import com.xiaomi_ws.bms.domain.rule.RuleExpression;
import com.xiaomi_ws.bms.domain.rule.WarningRule;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatteryType;
import com.xiaomi_ws.bms.infrastructure.entity.VehicleEntity;
import com.xiaomi_ws.bms.infrastructure.entity.WarningRuleEntity;
import org.springframework.stereotype.Component;

@Component
public class WarningRuleMapper {

    public WarningRule toDomain(WarningRuleEntity entity) {
        if (entity == null) {
            return null;
        }

        BatteryType batteryType = BatteryType.of(entity.getBatteryType().name());
        RuleExpression expression = new RuleExpression(entity.getRuleExpression());

        return new WarningRule(
                entity.getRuleCode(),
                entity.getRuleName(),
                batteryType,
                expression,
                entity.getStatus() == 1,
                entity.getCreateTime(),
                entity.getUpdateTime()
        );
    }

    public WarningRuleEntity toEntity(WarningRule domain) {
        if (domain == null) {
            return null;
        }

        WarningRuleEntity entity = new WarningRuleEntity();
        entity.setRuleCode(domain.getRuleCode());
        entity.setRuleName(domain.getRuleName());
        entity.setBatteryType(VehicleEntity.BatteryTypeEnum.valueOf(domain.getBatteryType().getValue()));
        entity.setRuleExpression(domain.getExpression().getExpression());
        entity.setStatus(domain.isEnabled() ? 1 : 0);

        return entity;
    }
}