package com.example.api.controller;

import com.example.api.model.dto.PageResponse;
import com.example.api.model.entity.Admin;
import com.example.api.model.entity.TExpress;
import com.example.api.model.entity.TExpressIn;
import com.example.api.model.entity.TExpressOut;
import com.example.api.service.AdminService;
import com.example.api.service.ExpressInService;
import com.example.api.service.ExpressOutService;
import com.example.api.service.ExpressService;
import com.example.api.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/express/out")
public class ExpressOutController {

    @Autowired
    private ExpressOutService expressOutService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ExpressInService expressInService;

    @Autowired
    private ExpressService expressService;

    @PostMapping("/save")
    public TExpressOut save(@RequestBody TExpressOut out, HttpServletRequest httpServletRequest) throws Exception {
        return expressOutService.save(out, httpServletRequest);
    }

    @GetMapping("/list")
    public PageResponse<TExpressOut> list(@RequestParam(required = false) String expressNo,
                                          @RequestParam(required = false) Integer pageNum,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Integer outType,
                                          @RequestParam(required = false) Integer outRole,
                                          HttpServletRequest httpServletRequest) {
        List<TExpressOut> filtered = expressOutService.findAll();
        Map<Long, TExpress> expressMap = expressService.findAll().stream().collect(Collectors.toMap(TExpress::getId, Function.identity()));
        Map<String, Admin> adminMap = adminService.findAll().stream().collect(Collectors.toMap(Admin::getId, Function.identity()));
        for (TExpressOut tExpressOut : filtered) {
            TExpress tExpress = expressMap.get(tExpressOut.getExpressId());
            if (tExpress == null) {
                tExpressOut.setExpressNo("该快递单号已删除");
            } else {
                tExpressOut.setExpressNo(tExpress.getExpressNo());
            }

            Admin inAdmin = adminMap.get(tExpressOut.getOutUserId());
            if (inAdmin == null) {
                tExpressOut.setOutUserEmail("该用户已删除");
            } else {
                tExpressOut.setOutUserEmail(inAdmin.getEmail());
            }
        }
        if (expressNo != null && !expressNo.trim().equals("")) {
            filtered = filtered.stream()
                    .filter(e -> e.getExpressNo() != null && e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }

        if (outType != null) {
            filtered = filtered.stream()
                    .filter(e -> e.getOutType() != null && e.getOutType().equals(outType))
                    .collect(Collectors.toList());
        }

        if (outRole != null) {
            filtered = filtered.stream()
                    .filter(e -> e.getOutRole() != null && e.getOutRole().equals(outRole))
                    .collect(Collectors.toList());
        }
        if (!JwtTokenUtil.isAdmin(httpServletRequest)) {
            String userId = JwtTokenUtil.getUserId(httpServletRequest);
            filtered = filtered.stream()
                    .filter(e -> e.getOutUserId() != null && e.getOutUserId().equals(userId))
                    .collect(Collectors.toList());
        }

        // 3. 手动分页
        int total = filtered.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<TExpressOut> pageData;
        if (startIndex > total) {
            pageData = new ArrayList<>();
        } else {
            pageData = filtered.subList(startIndex, endIndex);
        }

        PageResponse<TExpressOut> response = new PageResponse<>();
        response.setData(pageData);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotal((long) total);
        response.setTotalPages((int) Math.ceil((double) total / pageSize));

        return response;
    }
}
