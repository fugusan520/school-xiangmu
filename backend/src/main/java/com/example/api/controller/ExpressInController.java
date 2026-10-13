package com.example.api.controller;

import com.example.api.model.dto.PageResponse;
import com.example.api.model.entity.Admin;
import com.example.api.model.entity.TCabinet;
import com.example.api.model.entity.TExpress;
import com.example.api.model.entity.TExpressIn;
import com.example.api.service.AdminService;
import com.example.api.service.CabinetService;
import com.example.api.service.ExpressInService;
import com.example.api.service.ExpressService;
import com.example.api.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/express/in")
public class ExpressInController {

    @Autowired
    private ExpressInService expressInService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private CabinetService cabinetService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/save")
    public TExpressIn save(@RequestBody TExpressIn in, HttpServletRequest httpServletRequest) throws Exception {
        String token = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);
        System.out.println(JwtTokenUtil.getUserId(token));
        in.setInUserId(JwtTokenUtil.getUserId(token));
        in.setInTime(LocalDateTime.now());
        return expressInService.save(in);
    }

    @GetMapping("/list")
    public PageResponse<TExpressIn> list(
            @RequestParam(required = false) String expressNo,
            @RequestParam(required = false) String cabinetName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest httpServletRequest) {

        List<TExpressIn> filtered = expressInService.findAll();
        Map<Long, TExpress> expressMap = expressService.findAll().stream().collect(Collectors.toMap(TExpress::getId, Function.identity()));
        Map<Long, TCabinet> cabinetMap = cabinetService.findAll().stream().collect(Collectors.toMap(TCabinet::getId, Function.identity()));
        Map<String, Admin> adminMap = adminService.findAll().stream().collect(Collectors.toMap(Admin::getId, Function.identity()));
        for (TExpressIn expressIn : filtered) {
            TExpress tExpress = expressMap.get(expressIn.getExpressId());
            if (tExpress == null) {
                expressIn.setExpressNo("该快递单号已删除");
            } else {
                expressIn.setExpressNo(tExpress.getExpressNo());
            }
            TCabinet tCabinet = cabinetMap.get(expressIn.getCabinetId());
            if (tCabinet == null) {
                expressIn.setCabinetName("该快递柜已删除");
            } else {
                expressIn.setCabinetName(tCabinet.getCabinetName());
            }

            Admin inAdmin = adminMap.get(expressIn.getInUserId());
            if (inAdmin == null) {
                expressIn.setInEmail("该用户已删除");
            } else {
                expressIn.setInEmail(inAdmin.getEmail());
            }
            Admin outAdmin = adminMap.get(expressIn.getOutUserId());
            if (outAdmin == null) {
                expressIn.setOutEmail("该用户已删除");
            } else {
                expressIn.setOutEmail(outAdmin.getEmail());
            }
        }
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            filtered = filtered.stream()
                    .filter(e -> e.getOutUserId().equals(userId) || e.getInUserId().equals(userId))
                    .collect(Collectors.toList());
        }

        if (expressNo != null && !expressNo.trim().equals("")) {
            filtered = filtered.stream()
                    .filter(e -> e.getExpressNo() != null && e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }

        if (cabinetName != null && !cabinetName.trim().equals("")) {
            filtered = filtered.stream()
                    .filter(e -> e.getCabinetName() != null && e.getCabinetName().contains(cabinetName))
                    .collect(Collectors.toList());
        }

        if (status != null) {
            filtered = filtered.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        // 3. 手动分页
        int total = filtered.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<TExpressIn> pageData;
        if (startIndex > total) {
            pageData = new ArrayList<>();
        } else {
            pageData = filtered.subList(startIndex, endIndex);
        }

        PageResponse<TExpressIn> response = new PageResponse<>();
        response.setData(pageData);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotal((long) total);
        response.setTotalPages((int) Math.ceil((double) total / pageSize));

        return response;

    }

}
