package com.xiaomi_ws.bms.domain.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaomi_ws.bms.domain.shared.valueobject.BatterySignal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RuleEngine {

    private static final Pattern SIMPLE_CONDITION_PATTERN = Pattern.compile(
            "(\\w+)\\s*(-|\\+)\\s*(\\w+)\\s*(>=|<=|>|<)\\s*([\\d.]+)"
    );

    private static final Pattern RANGE_CONDITION_PATTERN = Pattern.compile(
            "(\\w+)\\s*(-|\\+)\\s*(\\w+)\\s*(>=|<=|>|<)\\s*([\\d.]+)\\s*&&\\s*(\\w+)\\s*(-|\\+)\\s*(\\w+)\\s*(>=|<=|>|<)\\s*([\\d.]+)"
    );

    public WarningResult evaluateRule(WarningRule rule, BatterySignal signal) {
        try {
            log.debug("开始评估规则: {}", rule.getRuleCode());
            log.debug("信号数据: Mx={}, Mi={}, Ix={}, Ii={}",
                    signal.getMx(), signal.getMi(), signal.getIx(), signal.getIi());

            String ruleExpression = rule.getExpression().getExpression();
            List<Map<String, Object>> conditions = JSON.parseObject(ruleExpression,
                    new TypeReference<List<Map<String, Object>>>() {});

            log.debug("解析到的条件列表: {}", conditions);

            // 按级别排序，从高到低（数值越大级别越高）
            conditions.sort((c1, c2) -> {
                Integer level1 = (Integer) c1.get("level");
                Integer level2 = (Integer) c2.get("level");
                return Integer.compare(level2, level1); // 降序排列
            });

            log.debug("按级别排序后的条件: {}", conditions);

            // 从最高级别开始检查
            for (Map<String, Object> condition : conditions) {
                String conditionExpr = (String) condition.get("condition");
                Integer level = (Integer) condition.get("level");

                log.debug("评估条件: '{}', 等级: {}", conditionExpr, level);

                boolean result = evaluateCondition(conditionExpr, signal);
                log.debug("条件 '{}' 评估结果: {}", conditionExpr, result);

                if (result) {
                    String desc = generateWarningDesc(rule.getRuleName(), level, signal);
                    log.info("触发预警: {}, 等级: {}, 描述: {}", rule.getRuleName(), level, desc);
                    return new WarningResult(true, level, rule.getRuleName(), desc);
                }
            }

            log.debug("所有条件都不满足，返回正常状态");
            return new WarningResult(false, -1, rule.getRuleName(), "正常");

        } catch (Exception e) {
            log.error("规则评估失败: {}, 规则: {}", e.getMessage(), rule.getRuleCode(), e);
            return new WarningResult(false, -1, rule.getRuleName(), "规则评估异常: " + e.getMessage());
        }
    }

    private boolean evaluateCondition(String condition, BatterySignal signal) {
        try {
            condition = condition.trim();
            log.debug("解析条件: '{}'", condition);

            // 处理复合条件 (&&)
            if (condition.contains("&&")) {
                return evaluateRangeCondition(condition, signal);
            }

            // 处理简单条件
            return evaluateSimpleCondition(condition, signal);

        } catch (Exception e) {
            log.error("条件评估失败: {}, condition: '{}'", e.getMessage(), condition, e);
            return false;
        }
    }

    private boolean evaluateRangeCondition(String condition, BatterySignal signal) {
        log.debug("评估范围条件: '{}'", condition);

        // 分割 && 条件
        String[] parts = condition.split("&&");
        if (parts.length != 2) {
            log.warn("范围条件格式错误: '{}'", condition);
            return false;
        }

        String condition1 = parts[0].trim();
        String condition2 = parts[1].trim();

        log.debug("范围条件部分1: '{}'", condition1);
        log.debug("范围条件部分2: '{}'", condition2);

        boolean result1 = evaluateSimpleCondition(condition1, signal);
        boolean result2 = evaluateSimpleCondition(condition2, signal);

        log.debug("范围条件结果: {} && {} = {}", result1, result2, result1 && result2);

        return result1 && result2;
    }

    private boolean evaluateSimpleCondition(String condition, BatterySignal signal) {
        log.debug("评估简单条件: '{}'", condition);

        Matcher matcher = SIMPLE_CONDITION_PATTERN.matcher(condition);
        if (matcher.find()) {
            String var1 = matcher.group(1);       // Mx
            String operator1 = matcher.group(2);  // -
            String var2 = matcher.group(3);       // Mi
            String comparison = matcher.group(4);  // >=
            String thresholdStr = matcher.group(5); // 0.5

            try {
                double value1 = getSignalValue(var1, signal);
                double value2 = getSignalValue(var2, signal);
                double threshold = Double.parseDouble(thresholdStr);

                // 计算表达式值
                double expressionValue;
                if ("-".equals(operator1)) {
                    expressionValue = value1 - value2;
                } else if ("+".equals(operator1)) {
                    expressionValue = value1 + value2;
                } else {
                    log.warn("不支持的运算符: {}", operator1);
                    return false;
                }

                log.debug("计算: {} {} {} = {} {} {} = {} {} {}",
                        var1, operator1, var2, value1, operator1, value2,
                        expressionValue, comparison, threshold);

                boolean result = compareValues(expressionValue, comparison, threshold);
                log.debug("比较结果: {} {} {} = {}", expressionValue, comparison, threshold, result);

                return result;

            } catch (NumberFormatException e) {
                log.error("无法解析阈值: {}", thresholdStr);
                return false;
            }
        }

        log.warn("无法解析简单条件: '{}'", condition);
        return false;
    }

    private double getSignalValue(String variable, BatterySignal signal) {
        double value;
        switch (variable) {
            case "Mx":
                value = signal.getMx() != null ? signal.getMx().doubleValue() : 0.0;
                break;
            case "Mi":
                value = signal.getMi() != null ? signal.getMi().doubleValue() : 0.0;
                break;
            case "Ix":
                value = signal.getIx() != null ? signal.getIx().doubleValue() : 0.0;
                break;
            case "Ii":
                value = signal.getIi() != null ? signal.getIi().doubleValue() : 0.0;
                break;
            default:
                log.warn("未知的信号变量: {}", variable);
                value = 0.0;
        }

        log.debug("获取信号值: {} = {}", variable, value);
        return value;
    }

    private boolean compareValues(double left, String operator, double right) {
        boolean result;
        switch (operator) {
            case ">=":
                result = left >= right;
                break;
            case "<=":
                result = left <= right;
                break;
            case ">":
                result = left > right;
                break;
            case "<":
                result = left < right;
                break;
            case "==":
                result = Math.abs(left - right) < 0.0001; // 浮点数比较
                break;
            case "!=":
                result = Math.abs(left - right) >= 0.0001;
                break;
            default:
                log.warn("不支持的操作符: {}", operator);
                result = false;
        }

        log.debug("数值比较: {} {} {} = {}", left, operator, right, result);
        return result;
    }

    private String generateWarningDesc(String ruleName, int level, BatterySignal signal) {
        StringBuilder desc = new StringBuilder();
        desc.append(ruleName).append(" - 等级").append(level).append("预警");

        if (signal.hasVoltageData()) {
            double voltageDiff = signal.getMx().doubleValue() - signal.getMi().doubleValue();
            desc.append(", 电压差: ").append(String.format("%.3f", voltageDiff));
            desc.append("V (Mx: ").append(signal.getMx()).append("V, Mi: ").append(signal.getMi()).append("V)");
        }

        if (signal.hasCurrentData()) {
            double currentDiff = signal.getIx().doubleValue() - signal.getIi().doubleValue();
            desc.append(", 电流差: ").append(String.format("%.3f", currentDiff));
            desc.append("A (Ix: ").append(signal.getIx()).append("A, Ii: ").append(signal.getIi()).append("A)");
        }

        return desc.toString();
    }

    @Data
    @AllArgsConstructor
    public static class WarningResult {
        private boolean isWarning;
        private int warningLevel;
        private String ruleName;
        private String description;
    }
}