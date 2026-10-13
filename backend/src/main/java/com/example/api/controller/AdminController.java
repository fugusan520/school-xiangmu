package com.example.api.controller;

import com.example.api.exception.AccountAndPasswordError;
import com.example.api.model.dto.LoginDto;
import com.example.api.model.entity.Admin;
import com.example.api.model.entity.LoginLog;
import com.example.api.model.enums.Role;
import com.example.api.model.support.ResponseResult;
import com.example.api.repository.AdminRepository;
import com.example.api.service.AdminService;
import com.example.api.service.LoginLogService;
import com.example.api.utils.JwtTokenUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    //获取日志对象
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private AdminService adminService;

    @Resource
    private AdminRepository adminRepository;

    @Resource
    private LoginLogService loginLogService;

    @GetMapping("hasInit")
    public boolean hasInit() {
        return adminRepository.existsAdminByRoles(Role.ROLE_SUPER_ADMIN.getValue());
    }

    @PostMapping("/init")
    public Admin init(@RequestBody Admin admin) throws Exception {
        System.out.println("热部署爽啊！！！");
        return adminService.save(admin, true);
    }

    @GetMapping("")
//    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN' ,'ROLE_ADMIN')")
    public List<Admin> findAll() {
        return adminService.findAll();
    }

    @DeleteMapping("")
//    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN' ,'ROLE_ADMIN')")
    public void delete(String id) {
        adminService.delete(id);
    }

    @PostMapping("")
//    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN' ,'ROLE_ADMIN')")
    public Admin save(@RequestBody Admin admin) throws Exception {
        if (admin.getRoles().contains(";")) {
            throw new Exception("不支持多个用户权限");
        }
        return adminService.save(admin, false);
    }

    @PostMapping("/login")
    public Map<String, Object> loginByEmail(String type, @RequestBody LoginDto dto, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Admin admin = null;
        String token = null;
        try {
            admin = type.equals("email") ? adminService.loginByEmail(dto) : adminService.loginByPassword(dto);
            token = adminService.createToken(admin,
                    dto.isRemember() ? JwtTokenUtil.REMEMBER_EXPIRATION_TIME : JwtTokenUtil.EXPIRATION_TIME);
        }catch (Exception e){
            throw new Exception("邮箱或密码错误");
        }finally {
            loginLogService.recordLog(dto,admin,request);
        }
        map.put("admin", admin);
        map.put("token", token);
        return map;
    }

    @GetMapping("/sendEmail")
    public ResponseResult sendEmail(String email) throws Exception {
        Boolean flag = adminService.sendEmail(email);
        ResponseResult res = new ResponseResult();
        if (flag){
            res.setMsg("发送成功，请登录邮箱查看");
        }else {
            res.setMsg("发送验证码失败，请检查邮箱服务");
        }
        res.setStatus(flag);
        return res;
    }


    @GetMapping("/findAdminByRole")
    public List<Admin> findAdminByRole(@RequestParam String role, HttpServletRequest httpServletRequest) {
        String userId = JwtTokenUtil.getUserId(httpServletRequest);
        if (JwtTokenUtil.isAdmin(httpServletRequest)) {
            return adminService.findAll().stream().filter(a -> role.equals(a.getRoles())).collect(Collectors.toList());
        } else {
            String roles = JwtTokenUtil.getTokenRoles(httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER)).get(0);
            if (!Objects.equals(roles, role)) {
                return adminService.findAll().stream().filter(a -> role.equals(a.getRoles())).collect(Collectors.toList());
            } else {
                return adminService.findAll().stream().filter(a -> userId.equals(a.getId())).collect(Collectors.toList());
            }
        }
    }


    @GetMapping("/getRoles")
    public Map<String, String> getRoles(HttpServletRequest httpServletRequest) {
        List<String> tokenRoles = JwtTokenUtil.getTokenRoles(httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER));
        String s = tokenRoles.get(0);
        Map<String, String> map = Maps.newHashMap();
        map.put("token", s);
        return map;
    }

}
