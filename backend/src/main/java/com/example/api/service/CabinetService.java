package com.example.api.service;

import com.example.api.model.entity.TCabinet;

import java.util.List;

public interface CabinetService {
    List<TCabinet> findAll();


    TCabinet save(TCabinet tCabinet);

    void deleteById(Long id);

    List<TCabinet> findByIds(List<Long> ids);

    TCabinet findById(Long id);
}
