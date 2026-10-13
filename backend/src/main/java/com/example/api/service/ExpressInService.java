package com.example.api.service;

import com.example.api.model.entity.TExpressIn;

import java.util.List;

public interface ExpressInService {

    TExpressIn save(TExpressIn in) throws Exception;

    List<TExpressIn> findAll();
}
