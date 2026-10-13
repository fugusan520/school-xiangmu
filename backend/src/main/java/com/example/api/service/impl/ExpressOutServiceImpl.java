package com.example.api.service.impl;
import com.example.api.model.entity.*;
import com.example.api.model.enums.Role;
import com.example.api.repository.*;
import com.example.api.service.AdminService;
import com.example.api.service.ExpressOutService;
import com.example.api.service.InventoryRecordService;
import com.example.api.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpressOutServiceImpl implements ExpressOutService {

    @Autowired
    private TExpressRepository expressRepository;
    @Autowired
    private TExpressOutRepository expressOutRepository;
    @Autowired
    private TExpressInRepository expressInRepository;
    @Autowired
    private TExpressStockRepository expressStockRepository;
    @Autowired
    private TCabinetBoxRepository cabinetBoxRepository;
    @Autowired
    private InventoryRecordService inventoryRecordService;

    @Autowired
    private AdminService adminService;


    @Override
    @Transactional
    public TExpressOut save(TExpressOut out, HttpServletRequest httpServletRequest) throws Exception {


        // 1️⃣ 校验快递
        TExpress express = expressRepository.findById(out.getExpressId())
                .orElseThrow(() -> new RuntimeException("快递不存在"));

        // 2️⃣ 查询入库记录
        TExpressIn in = expressInRepository
                .findByExpressIdAndStatus(express.getId(), 0)
                .orElseThrow(() -> new RuntimeException("快递未入库或已取件"));

        // 3️⃣ 校验验证码（用户取件才校验）
        if (out.getOutRole() != null) {
            if (!out.getVerifyCode().equals(in.getVerifyCode())) {
                throw new RuntimeException("验证码错误");
            }
        }
        if (!out.getOutType().equals(in.getInType())) {
            throw new RuntimeException("出库类型与入库类型不匹配");
        }
        // 权限校验
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            String roles = JwtTokenUtil.getTokenRoles(httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER)).get(0);
            if (!in.getOutUserId().equals(userId)) {
                throw new RuntimeException("当前用户没有权限出库");
            }
            if (roles.equals(Role.ROLE_USER.getValue()) && out.getOutType() == 2) {
                throw new RuntimeException("用户无工作人员发货权限！");
            }
            if (roles.equals(Role.ROLE_COURIER.getValue()) && out.getOutType() == 1) {
                throw new RuntimeException("工作人员无用户取件权限！");
            }
        }
        // 4️⃣ 释放柜口
        TCabinetBox box = cabinetBoxRepository
                .findByCabinetIdAndBoxNo(in.getCabinetId(), in.getBoxNo())
                .orElseThrow(() -> new RuntimeException("柜口不存在"));
        box.setStatus(0);
        cabinetBoxRepository.save(box);

        // 5️⃣ 更新入库状态
        in.setStatus(1);
        expressInRepository.save(in);

        // 6️⃣ 更新库存
        TExpressStock stock = expressStockRepository
                .findByExpressIdAndStatus(express.getId(), 0)
                .orElseThrow(() -> new RuntimeException("库存异常"));
        stock.setStatus(1);
        expressStockRepository.save(stock);

        // 7️⃣ 写出库记录
        out.setOutTime(LocalDateTime.now());
        expressOutRepository.save(out);

        // 8️⃣ 更新快递状态
        if (out.getOutType().equals(1)) {
            express.setStatus(2);
        } else {
            express.setStatus(3);
        }
        expressRepository.save(express);


        // 9 生成通知
        Admin outUser = adminService.findById(JwtTokenUtil.getUserId(httpServletRequest));
        inventoryRecordService.saveExpressLog(in.getInUserId(), express.getExpressNo(), outUser.getEmail(),
                express.getBizType(), null, null, null, 2);
        inventoryRecordService.saveExpressLog(in.getOutUserId(), express.getExpressNo(), outUser.getEmail(),
                express.getBizType(), null, null, null, 2);
        return out;
    }
    @Override
    public List<TExpressOut> findAll() {
        return expressOutRepository.findAll();
    }
}
