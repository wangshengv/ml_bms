package com.xiaomi_ws.bms.applications.service;

import com.xiaomi_ws.bms.domain.signal.BatterySignal;
import com.xiaomi_ws.bms.domain.signal.BatterySignalRepository;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalRequest;
import com.xiaomi_ws.bms.interfaces.dto.BatterySignalUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatterySignalService {

    private final BatterySignalRepository batterySignalRepository;

    public void saveBatterySignal(BatterySignalRequest request) {
        BatterySignal signal = new BatterySignal();
        signal.setVid(request.getVid());
        signal.setChassisNumber(request.getChassisNumber());
        signal.setMx(request.getMx());
        signal.setMi(request.getMi());
        signal.setIx(request.getIx());
        signal.setIi(request.getIi());
        signal.setCreateTime(java.time.LocalDateTime.now());
        batterySignalRepository.save(signal);
        log.debug("保存电池信号数据: vid={}, signal={}", request.getVid(), signal);
    }

    public List<BatterySignal> getBatterySignalsByVid(String vid) {
        return batterySignalRepository.findByVid(vid);
    }

    public BatterySignal getBatterySignalById(Long id) {
        return batterySignalRepository.findById(id);
    }

    public void deleteBatterySignal(Long id) {
        batterySignalRepository.deleteById(id);
        log.debug("删除电池信号数据: id={}", id);
    }

    public void updateBatterySignal(BatterySignalUpdateRequest request) {
        BatterySignal signal = new BatterySignal();
        signal.setId(request.getId());
        signal.setVid(request.getVid());
        signal.setChassisNumber(request.getChassisNumber());
        signal.setMx(request.getMx());
        signal.setMi(request.getMi());
        signal.setIx(request.getIx());
        signal.setIi(request.getIi());
        signal.setCreateTime(java.time.LocalDateTime.now());
        batterySignalRepository.update(signal);
        log.debug("更新电池信号数据: id={}, vid={}", request.getId(), request.getVid());
    }
}
