package com.example.api.model.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_cabinet_box")
public class TCabinetBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cabinet_id", nullable = false)
    private Long cabinetId;

    @Column(name = "box_no", nullable = false, length = 20)
    private String boxNo;

    @Column(name = "box_type")
    private Integer boxType;

    @Column(name = "status")
    private Integer status = 0;


    private String cabinetName;
}