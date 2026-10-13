package com.example.api.service;

import com.example.api.model.entity.TCabinetBox;

import java.util.List;

public interface CabinetBoxService {
    List<TCabinetBox> findAll();

    List<TCabinetBox> findByCabinetId(Long cabinetId);

    TCabinetBox findById(Long id) throws Exception;

    void delete(Long id);

    void deleteAll(List<Long> ids);

    void update(TCabinetBox box);

    TCabinetBox save(TCabinetBox box);

    void saveAll(List<TCabinetBox> list);

    List<TCabinetBox> findByCabinetIds(List<Long> cabinetIds);
}
