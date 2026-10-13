package com.example.api.service.impl;

import com.example.api.exception.AccountAndPasswordError;
import com.example.api.model.dto.LoginDto;
import com.example.api.model.entity.Admin;
import com.example.api.model.entity.LoginLog;
import com.example.api.model.enums.Role;
import com.example.api.repository.AdminRepository;
import com.example.api.repository.LoginLogRepository;
import com.example.api.service.AdminService;
import com.example.api.service.EmailService;
import com.example.api.utils.DataTimeUtil;
import com.example.api.utils.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminRepository adminRepository;

    @Resource
    private EmailService emailService;

    @Override
    public Admin save(Admin admin, boolean isInit) throws Exception {

        // 校验逻辑
        if (admin.getEmail() == null || admin.getPassword() == null) {
            throw new Exception("用户名和密码不能为空");
        }



        // 密码校验
        String password = admin.getPassword();
        if (password.length() < 6) {
            throw new Exception("密码长度不能少于6位");
        }
        Admin adminByEmail = adminRepository.findAdminByEmail(admin.getEmail());
        if (adminByEmail != null && isInit) {
            throw new Exception("当前用户名已注册！");
        }
        admin.setCreateAt(DataTimeUtil.getNowTimeString());
        return adminRepository.save(admin);
    }

    @Override
    public Admin findById(String id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public boolean sendEmail(String email) throws Exception {
        Admin admin = adminRepository.findAdminByEmail(email);
        if (admin == null) throw new Exception("不存在的邮箱账户");
        return emailService.sendVerificationCode(email);
    }

    @Override
    public Admin loginByPassword(LoginDto dto) throws Exception {
        Admin one = adminRepository.findAdminByEmailAndPassword(dto.getEmail(), dto.getPassword());
        if (one == null) {
            throw new Exception("用户名或密码错误");
        }
        return one;
    }

    @Override
    public Admin loginByEmail(LoginDto dto) throws Exception {
        boolean status = emailService.checkVerificationCode(dto.getEmail(), dto.getCode());
        if (!status) throw new Exception("验证码错误");
        return adminRepository.findAdminByEmail(dto.getEmail());
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();

    }

    @Override
    public String createToken(Admin admin, long exp) {
        String rolesString = admin.getRoles();
        String[] roles = rolesString != null ? rolesString.split(";") : null;
        return JwtTokenUtil.createToken(admin.getId(), roles, exp);
    }

    @Override
    public void delete(String id) {
        adminRepository.deleteById(id);
    }

}
