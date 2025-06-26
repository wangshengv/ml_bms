package com.xiaomi_ws.bms.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponseDto<T> {
    private Integer status;
    private String msg;
    private T data;

    public static <T> ApiResponseDto<T> success(T data) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setStatus(200);
        response.setMsg("ok");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponseDto<T> error(String message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setStatus(500);
        response.setMsg(message);
        return response;
    }
}
