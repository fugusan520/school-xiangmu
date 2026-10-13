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
@Table(name = "t_express_stock")
public class TExpressStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "express_id", nullable = false)
    private Long expressId;

    @Column(name = "cabinet_id")
    private Long cabinetId;

    @Column(name = "box_no", length = 20)
    private String boxNo;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    private String expressNo;

    private String cabinetName;
}