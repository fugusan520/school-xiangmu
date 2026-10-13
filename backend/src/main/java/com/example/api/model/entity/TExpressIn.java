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
@Table(name = "t_express_in")
public class TExpressIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "express_id", nullable = false)
    private Long expressId;

    @Column(name = "cabinet_id")
    private Long cabinetId;

    @Column(name = "box_no", length = 20)
    private String boxNo;

    @Column(name = "in_user_id")
    private String inUserId;

    @Column(name = "out_user_id")
    private String outUserId;

    @Column(name = "in_type")
    private Integer inType = 1;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    @Column(name = "verify_code", length = 10)
    private String verifyCode;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "status")
    private Integer status = 0;


    private String expressNo;

    private String cabinetName;

    private String inEmail;
    private String outEmail;
}