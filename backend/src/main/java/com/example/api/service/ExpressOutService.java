package com.example.api.service;

import com.example.api.model.entity.TExpressOut;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface ExpressOutService {


    TExpressOut save(TExpressOut out, HttpServletRequest httpServletRequest) throws Exception;

    List<TExpressOut> findAll();
}
