package com.example.api.service.impl;

import com.example.api.model.entity.*;
import com.example.api.repository.*;
import com.example.api.service.AdminService;
import com.example.api.service.CabinetService;
import com.example.api.service.ExpressInService;
import com.example.api.service.InventoryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ExpressInServiceImpl implements ExpressInService {

    @Autowired
    private TExpressRepository expressRepository;
    @Autowired
    private TExpressInRepository expressInRepository;
    @Autowired
    private TExpressStockRepository expressStockRepository;
    @Autowired
    private TCabinetBoxRepository cabinetBoxRepository;

    @Autowired
    private CabinetService cabinetService;

    @Autowired
    private InventoryRecordService inventoryRecordService;

    @Autowired
    private AdminService adminService;


    @Override
    public List<TExpressIn> findAll() {
        return expressInRepository.findAll();
    }

    @Override
    @Transactional
    public TExpressIn save(TExpressIn in) throws Exception {
        Integer inType = in.getInType();

        // 1️⃣ 校验快递是否存在
        TExpress express = expressRepository.findById(in.getExpressId())
                .orElseThrow(() -> new RuntimeException("快递不存在"));
        if (!express.getBizType().equals(inType)) {
            throw new Exception("入库类型与该快递单号的业务类型不符！");
        }

        // 2️⃣ 查找空闲柜口
        TCabinetBox box = cabinetBoxRepository
                .findByCabinetIdAndBoxNo(in.getCabinetId(), in.getBoxNo())
                .orElseThrow(() -> new RuntimeException("快递柜口查询失败"));
        TCabinet cabinet = cabinetService.findById(in.getCabinetId());

        // 3️⃣ 占用柜口
        box.setStatus(1);
        cabinetBoxRepository.save(box);

        // 4️⃣ 生成验证码
        String verifyCode = generateVerifyCode();

        // 5️⃣ 写入入库记录
        in.setCabinetId(in.getCabinetId());
        in.setBoxNo(in.getBoxNo());
        in.setVerifyCode(verifyCode);
        in.setStatus(0);
        expressInRepository.save(in);

        // 6️⃣ 写库存表
        TExpressStock stock = new TExpressStock();
        stock.setExpressId(express.getId());
        stock.setCabinetId(in.getCabinetId());
        stock.setBoxNo(in.getBoxNo());
        stock.setStatus(0);
        stock.setInTime(LocalDateTime.now());
        expressStockRepository.save(stock);

        // 7️⃣ 更新快递状态
        express.setStatus(1); // 已入库
        expressRepository.save(express);

        // 8 生成通知
        Admin inAdmin = adminService.findById(in.getInUserId());
        inventoryRecordService.saveExpressLog(in.getOutUserId(), express.getExpressNo(), inAdmin.getEmail(),
                express.getBizType(), box, cabinet, verifyCode, 1);

        return in;
    }

    private String generateVerifyCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
    }

}
