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
@Table(name = "t_cabinet")
public class TCabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cabinet_no", nullable = false, length = 32)
    private String cabinetNo;

    @Column(name = "cabinet_name", length = 50)
    private String cabinetName;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "total_boxes")
    private Integer totalBoxes;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}