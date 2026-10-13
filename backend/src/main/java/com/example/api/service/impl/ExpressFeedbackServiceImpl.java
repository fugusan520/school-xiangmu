package com.example.api.service.impl;

import com.example.api.model.entity.ExpressFeedback;
import com.example.api.repository.ExpressFeedbackRepository;
import com.example.api.service.ExpressFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Taokn
 * @description
 * @date 2026/2/9 16:21
 */
@Service
public class ExpressFeedbackServiceImpl implements ExpressFeedbackService {

    @Autowired
    private ExpressFeedbackRepository expressFeedbackRepository;

    @Override
    public List<ExpressFeedback> listByAdminId(String adminId) {
        return expressFeedbackRepository.findByAdminId(adminId);
    }

    @Override
    public void deleteById(Long id) {
        expressFeedbackRepository.deleteById(id);
    }

    @Override
    public ExpressFeedback save(ExpressFeedback feedback) {
        return expressFeedbackRepository.save(feedback);
    }

    @Override
    public List<ExpressFeedback> listAll() {
        return expressFeedbackRepository.findAll();
    }

    @Override
    public List<ExpressFeedback> findByExpressNo(String expressNo) {
        return expressFeedbackRepository.findByExpressNo(expressNo);
    }

}

