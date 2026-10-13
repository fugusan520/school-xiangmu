package com.example.api.controller;

import antlr.StringUtils;
import com.example.api.model.dto.PageResponse;
import com.example.api.model.entity.Admin;
import com.example.api.model.entity.TExpress;
import com.example.api.model.entity.TExpressIn;
import com.example.api.model.enums.Role;
import com.example.api.service.AdminService;
import com.example.api.service.ExpressInService;
import com.example.api.service.ExpressService;
import com.example.api.utils.JwtTokenUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ExpressInService expressInService;

    @PostMapping("/save")
    public TExpress save(@RequestBody TExpress express, HttpServletRequest httpServletRequest) throws Exception {


        if (express.getExpressNo() == null || express.getExpressNo().trim().isEmpty()) {
            throw new IllegalArgumentException("快递单号不能为空");
        }
        if (express.getCompanyName() == null || express.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("快递公司不能为空");
        }
        if (express.getSenderName() == null || express.getSenderName().trim().isEmpty()) {
            throw new IllegalArgumentException("寄件人不能为空");
        }
        if (express.getSenderPhone() == null || express.getSenderPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("寄件电话不能为空");
        }
        if (express.getReceiverName() == null || express.getReceiverName().trim().isEmpty()) {
            throw new IllegalArgumentException("收件人不能为空");
        }
        if (express.getReceiverPhone() == null || express.getReceiverPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("收件电话不能为空");
        }
        if (express.getReceiverAddress() == null || express.getReceiverAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("收件地址不能为空");
        }
        if (express.getWeight() == null || express.getWeight().longValue() <= BigDecimal.ZERO.longValue()) {
            throw new IllegalArgumentException("重量必须大于 0");
        }



        String token = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);
        String role = JwtTokenUtil.getTokenRoles(token).get(0);
        if (role.equals(Role.ROLE_USER.getValue())) {
            if (express.getBizType() == 1) {
                throw new Exception("用户的业务类型仅支持寄件");
            }
        }
        if (role.equals(Role.ROLE_COURIER.getValue())) {
            if (express.getBizType() == 2) {
                throw new Exception("工作人员的业务类型仅支持收件");
            }
        }
        express.setCreateUserId(JwtTokenUtil.getUserId(token));
        express.setCreateTime(LocalDateTime.now());
        return expressService.save(express);
    }

    @GetMapping("/list")
    public PageResponse<TExpress> list(@RequestParam(required = false) String expressNo,
                                       @RequestParam(required = false) String receiverName,
                                       @RequestParam(required = false) Integer status,
                                       @RequestParam(required = false) String createUserId,
                                       @RequestParam(required = false) Integer pageNum,
                                       @RequestParam(required = false) Integer pageSize,
                                       HttpServletRequest httpServletRequest) {
        // 1. 获取所有数据
        List<TExpress> all = expressService.findAll();

        // 2. 流式过滤（按你的写法）
        List<TExpress> filtered = all;

        if (expressNo != null && !expressNo.trim().equals("")) {
            filtered = filtered.stream()
                    .filter(e -> e.getExpressNo() != null && e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }

        if (receiverName != null && !receiverName.trim().equals("")) {
            filtered = filtered.stream()
                    .filter(e -> e.getReceiverName() != null && e.getReceiverName().contains(receiverName))
                    .collect(Collectors.toList());
        }

        if (status != null) {
            filtered = filtered.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            Map<Long, TExpressIn> expressInMap =
                    expressInService.findAll().stream().collect(Collectors.toMap(TExpressIn::getExpressId, Function.identity()));
            filtered = filtered.stream()
                    .filter(e -> {
                        TExpressIn tExpressIn = expressInMap.get(e.getId());
                        boolean isCreator = e.getCreateUserId() != null && e.getCreateUserId().equals(userId);
                        boolean isOutUser = tExpressIn != null && userId.equals(tExpressIn.getOutUserId());
                        return isCreator || isOutUser;
                    })
                    .collect(Collectors.toList());
        }

        // 3. 手动分页
        int total = filtered.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<TExpress> pageData;
        if (startIndex > total) {
            pageData = new ArrayList<>();
        } else {
            pageData = filtered.subList(startIndex, endIndex);
        }

        PageResponse<TExpress> response = new PageResponse<>();
        response.setData(pageData);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotal((long) total);
        response.setTotalPages((int) Math.ceil((double) total / pageSize));

        return response;
    }

    @GetMapping("/{id}")
    public TExpress detail(@PathVariable Long id) {
        return expressService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        expressService.deleteById(Long.valueOf(id));
    }



    @GetMapping("/listByStatus")
    public List<TExpress> listByType(@RequestParam(required = false) Integer status, HttpServletRequest httpServletRequest) {
        // 1. 获取所有数据
        String userId = JwtTokenUtil.getUserId(httpServletRequest);

        List<TExpress> all = expressService.findAll();
        Map<Long, TExpressIn> expressInMap =
                expressInService.findAll().stream().collect(Collectors.toMap(TExpressIn::getExpressId, Function.identity()));
        // 2. 流式过滤（按你的写法）
        List<TExpress> filtered = all;
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            filtered = filtered.stream()
                    .filter(e -> {
                        TExpressIn tExpressIn = expressInMap.get(e.getId());
                        boolean isCreator = e.getCreateUserId() != null && e.getCreateUserId().equals(userId);
                        boolean isOutUser = tExpressIn != null && userId.equals(tExpressIn.getOutUserId());
                        return isCreator || isOutUser;
                    })
                    .collect(Collectors.toList());
        }

        if (status != null) {
            filtered = filtered.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        return filtered;
    }
}
