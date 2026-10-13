package com.example.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_express_out")
public class TExpressOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "express_id", nullable = false)
    private Long expressId;

    @Column(name = "out_type")
    private Integer outType;

    @Column(name = "out_user_id")
    private String outUserId;

    @Column(name = "out_role")
    private Integer outRole;

    @Column(name = "out_time")
    private LocalDateTime outTime;

    @Column(name = "remark", length = 255)
    private String remark;


    private String expressNo;

    private String outUserEmail;
    // 取件码
    private String verifyCode;
}