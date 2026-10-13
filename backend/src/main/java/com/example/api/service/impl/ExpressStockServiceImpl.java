package com.example.api.service.impl;

import com.example.api.model.entity.TExpressStock;
import com.example.api.repository.TExpressStockRepository;
import com.example.api.service.ExpressStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressStockServiceImpl implements ExpressStockService {

    @Autowired
    private TExpressStockRepository expressStockRepository;

    @Override
    public List<TExpressStock> findAll() {
        return expressStockRepository.findAll();
    }
}
