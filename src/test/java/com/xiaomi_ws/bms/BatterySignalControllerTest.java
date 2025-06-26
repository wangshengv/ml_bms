package com.xiaomi_ws.bms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalRequest;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BatterySignalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testReportBatterySignal() throws Exception {
        BatterySignalRequest request = new BatterySignalRequest(
                "1234567890ABCDEF",
                "CHASSIS123456",
                new BigDecimal("4.2"),
                new BigDecimal("3.8"),
                new BigDecimal("100.0"),
                new BigDecimal("80.0")
        );

        mockMvc.perform(post("/api/battery-signals/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetBatterySignals() throws Exception {
        mockMvc.perform(get("/api/battery-signals/getBatterySignals/1234567890ABCDEF"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetBatterySignalById() throws Exception {
        mockMvc.perform(get("/api/battery-signals/getBatterySignalById/48"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testUpdateBatterySignal() throws Exception {
        BatterySignalUpdateRequest request = new BatterySignalUpdateRequest(
                48L,
                "1234567890ABCDEF",
                "CHASSIS123456",
                new BigDecimal("4.1"),
                new BigDecimal("3.7"),
                new BigDecimal("90.0"),
                new BigDecimal("70.0")
        );

        mockMvc.perform(put("/api/battery-signals/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testDeleteBatterySignal() throws Exception {
        mockMvc.perform(delete("/api/battery-signals/delete/48"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
