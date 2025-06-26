package com.xiaomi_ws.bms.domain.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Value;

@Getter
public class RuleExpression {
    private final String expression;

    public RuleExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("规则表达式不能为空");
        }
        this.expression = expression;
    }
}
