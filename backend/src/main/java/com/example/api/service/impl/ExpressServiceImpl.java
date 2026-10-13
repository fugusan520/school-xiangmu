package com.example.api.service.impl;


import com.example.api.model.entity.*;
import com.example.api.repository.TExpressRepository;
import com.example.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private TExpressRepository expressRepository;
    @Autowired
    private AdminService adminService;


    @Override
    public TExpress save(TExpress express) {
        return expressRepository.save(express);
    }

    @Override
    public List<TExpress> findAll() {
        List<TExpress> all = expressRepository.findAll();
        all.forEach(a -> {
            if (a.getCreateUserId() == null) {
                return;
            }
            Admin byId = adminService.findById(a.getCreateUserId());
            if (byId != null) {
                a.setUserName(byId.getEmail());
            } else {
                a.setUserName("未找到该用户！");
            }
        });
        return all;
    }

    @Override
    public TExpress findById(Long id) {
        return expressRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        TExpress express = expressRepository.findById(id).orElse(null);
        if (express == null || express.getStatus() != 0) {
            throw new RuntimeException("状态非待入库，不允许删除该快递！");
        }
        expressRepository.deleteById(id);
    }
}
