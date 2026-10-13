package com.example.api.service.impl;

import com.example.api.model.entity.TCabinet;
import com.example.api.repository.TCabinetRepository;
import com.example.api.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetServiceImpl implements CabinetService {

    @Autowired
    private TCabinetRepository cabinetRepository;

    @Override
    public List<TCabinet> findAll() {
        return cabinetRepository.findAll();
    }

    @Override
    public TCabinet save(TCabinet tCabinet) {
        return cabinetRepository.save(tCabinet);
    }

    @Override
    public void deleteById(Long id) {
        cabinetRepository.deleteById(id);
    }

    @Override
    public List<TCabinet> findByIds(List<Long> ids) {
        return cabinetRepository.findAllById(ids);
    }

    @Override
    public TCabinet findById(Long id) {
        return cabinetRepository.findById(id).orElse(null);
    }
}
