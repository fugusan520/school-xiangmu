package com.example.api.service;

import com.example.api.model.entity.TExpress;

import java.util.List;

public interface ExpressService {

    TExpress save(TExpress express);

    List<TExpress> findAll();

    TExpress findById(Long id);

    void deleteById(Long id);
}
