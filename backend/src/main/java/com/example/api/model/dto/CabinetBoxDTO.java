package com.example.api.model.dto;

import com.example.api.model.entity.TCabinetBox;
import lombok.Data;

import java.util.List;

/**
 * @author Taokn
 * @description
 * @date 2026/2/7 13:05
 */
@Data
public class CabinetBoxDTO {
    private Long cabinetId;
    private String cabinetName;
    private String location;
    private List<TCabinetBox> boxes;
}