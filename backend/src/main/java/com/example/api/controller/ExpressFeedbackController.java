package com.example.api.controller;

import com.example.api.model.entity.Admin;
import com.example.api.model.entity.ExpressFeedback;
import com.example.api.model.entity.TExpress;
import com.example.api.model.entity.TExpressIn;
import com.example.api.service.AdminService;
import com.example.api.service.ExpressFeedbackService;
import com.example.api.service.ExpressInService;
import com.example.api.service.ExpressService;
import com.example.api.utils.JwtTokenUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Taokn
 * @description
 * @date 2026/2/9 16:22
 */
@RestController
@RequestMapping("/api/expressFeedback")
public class ExpressFeedbackController {

    @Autowired
    private ExpressFeedbackService expressFeedbackService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private ExpressInService expressInService;


    @GetMapping("/list")
    public List<ExpressFeedback> list(HttpServletRequest httpServletRequest, @RequestParam(required = false) String expressNo) {
        List<ExpressFeedback> result = expressFeedbackService.listAll();
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            result = result.stream()
                    .filter(e -> e.getAdminId().equals(userId))
                    .collect(Collectors.toList());
        }
        if (expressNo != null && !expressNo.trim().equals("")) {
            result = result.stream()
                    .filter(e -> e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }
        Map<String, Admin> adminMap = adminService.findAll().stream().collect(Collectors.toMap(Admin::getId, Function.identity()));
        for (ExpressFeedback expressFeedback : result) {
            expressFeedback.setName(adminMap.get(expressFeedback.getAdminId()) != null ? adminMap.get(expressFeedback.getAdminId()).getEmail() : "未知用户");
        }
        return result;

    }

    @PostMapping("/save")
    public ExpressFeedback save(@RequestBody ExpressFeedback feedback, HttpServletRequest httpServletRequest) {
        List<TExpress> all = expressService.findAll();
        if (all.stream().noneMatch(a -> a.getExpressNo().equals(feedback.getExpressNo()))) {
            throw new RuntimeException("未找到该快递:" + feedback.getExpressNo());
        }
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            Map<Long, TExpressIn> expressInMap =
                    expressInService.findAll().stream().collect(Collectors.toMap(TExpressIn::getExpressId, Function.identity()));
            all = all.stream()
                    .filter(e -> {
                        TExpressIn tExpressIn = expressInMap.get(e.getId());
                        boolean isCreator = e.getCreateUserId() != null && e.getCreateUserId().equals(userId);
                        boolean isOutUser = tExpressIn != null && userId.equals(tExpressIn.getOutUserId());
                        return isCreator || isOutUser;
                    })
                    .collect(Collectors.toList());
        }
        if (!JwtTokenUtil.isAdmin(httpServletRequest)
                && all.stream().noneMatch(a -> a.getExpressNo().equals(feedback.getExpressNo()))) {
            throw new RuntimeException("您对改快递无评价权限:" + feedback.getExpressNo());
        }
        if (expressFeedbackService.findByExpressNo(feedback.getExpressNo()).size() != 0) {
            throw new RuntimeException("单条快递只能保存一条评价！");
        }
        feedback.setAdminId(JwtTokenUtil.getUserId(httpServletRequest));
        return expressFeedbackService.save(feedback);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        expressFeedbackService.deleteById(id);
    }

    @PostMapping("/update")
    public void update(@RequestBody ExpressFeedback feedback, HttpServletRequest httpServletRequest) {
        expressFeedbackService.save(feedback);
    }


}
