package com.xiaomi_ws.bms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WarningControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProcessWarning() throws Exception {
        String requestBody = """
        [
          {
            "carId": 1,
            "warnId": 1,
            "signal": "{\\"Mx\\":12.0,\\"Mi\\":0.6}"
          },
          {
            "carId": 2,
            "warnId": 2,
            "signal": "{\\"Ix\\":12.0,\\"Ii\\":11.7}"
          },
          {
            "carId": 3,
            "signal": "{\\"Mx\\":11.0,\\"Mi\\":9.6,\\"Ix\\":12.0,\\"Ii\\":11.7}"
          }
        ]
        """;
        mockMvc.perform(post("/api/warn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print()) // 打印请求和响应详细信息
                .andExpect(status().isOk());
    }

    @Test
    void testProcessWarningNoWarnId() throws Exception {
        String requestBody = """
        [
          {
            "carId": 1,
            "signal": "{\\"Mx\\":12.0,\\"Mi\\":0.6}"
          }
        ]
        """;
        mockMvc.perform(post("/api/warn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print()) // 打印请求和响应详细信息
                .andExpect(status().isOk());
    }

    @Test
    void testGetVehicleWarnings() throws Exception {
        // 示例车辆ID，长度16位
        String vid = "VH8K2L9P3M6N4Q7R";

        mockMvc.perform(get("/api/warnings/" + vid))
                .andDo(print()) // 打印请求 & 响应信息（含 Body）
                .andExpect(status().isOk());
    }
}
