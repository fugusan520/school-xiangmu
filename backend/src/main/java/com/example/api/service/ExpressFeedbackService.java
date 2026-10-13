package com.example.api.service;

import com.example.api.model.entity.ExpressFeedback;

import java.util.List;

/**
 * @author Taokn
 * @description
 * @date 2026/2/9 16:21
 */
public interface ExpressFeedbackService {

    /**
     * 根据管理员ID查询评价记录
     */
    List<ExpressFeedback> listByAdminId(String adminId);

    /**
     * 根据ID删除评价
     */
    void deleteById(Long id);

    /**
     * 保存评价
     */
    ExpressFeedback save(ExpressFeedback feedback);

    List<ExpressFeedback> listAll();
    List<ExpressFeedback> findByExpressNo(String expressNo);

}
