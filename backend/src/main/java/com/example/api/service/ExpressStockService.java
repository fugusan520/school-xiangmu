package com.example.api.service;

import com.example.api.model.entity.TExpressStock;

import java.util.List;

public interface ExpressStockService {
    List<TExpressStock> findAll();
}
