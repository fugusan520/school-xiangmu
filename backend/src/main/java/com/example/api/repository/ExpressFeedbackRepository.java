package com.example.api.repository;

import com.example.api.model.entity.Employee;
import com.example.api.model.entity.ExpressFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Taokn
 * @description
 * @date 2026/2/9 16:18
 */
public interface ExpressFeedbackRepository extends JpaRepository<ExpressFeedback, Long> {
    List<ExpressFeedback> findByAdminId(String adminId);

    List<ExpressFeedback> findByExpressNo(String expressNo);

}
