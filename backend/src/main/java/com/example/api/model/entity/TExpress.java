package com.example.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_express")
public class TExpress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "express_no", nullable = false, length = 64)
    private String expressNo;

    @Column(name = "company_code", length = 32)
    private String companyCode;

    @Column(name = "company_name", length = 50)
    private String companyName;

    @Column(name = "sender_name", length = 50)
    private String senderName;

    @Column(name = "sender_phone", length = 20)
    private String senderPhone;

    @Column(name = "receiver_name", length = 50)
    private String receiverName;

    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;

    @Column(name = "receiver_address", length = 255)
    private String receiverAddress;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "remark", length = 255)
    private String remark;

    @Column(name = "biz_type")
    private Integer bizType = 1;

    @Column(name = "create_user_id")
    private String createUserId;

    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private String userName;
}