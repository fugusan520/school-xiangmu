package com.example.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Taokn
 * @description
 * @date 2026/2/9 16:16
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "express_feedback")
public class ExpressFeedback {

    /**
     * 评价ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 快递单号
     */
    @Column(name = "express_no", nullable = false, length = 200)
    private String expressNo;

    /**
     * 登记人的ID（管理员ID）
     */
    @Column(name = "admin_id", nullable = false, length = 200)
    private String adminId;

    /**
     * 评价内容
     */
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /**
     * 评价等级（0-5）
     */
    @Column(name = "rating")
    private Integer rating = 0;

    /**
     * 评价人联系方式
     */
    @Column(name = "contact_info", length = 100)
    private String contactInfo;

    /**
     * 状态：0-待处理，1-已处理，2-已忽略
     */
    @Column(name = "status")
    private Integer status = 0;

    /**
     * 评价时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    private String name;
}
