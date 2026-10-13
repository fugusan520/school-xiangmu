package com.example.api.repository;

import com.example.api.model.entity.TExpressOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TExpressOutRepository extends JpaRepository<TExpressOut, Long> {

    // 根据快递ID查询
    List<TExpressOut> findByExpressId(Long expressId);

    // 根据出库类型查询
    List<TExpressOut> findByOutType(Integer outType);

    // 根据出库操作人查询
    List<TExpressOut> findByOutUserId(Long outUserId);

    // 根据出库角色查询
    List<TExpressOut> findByOutRole(Integer outRole);

    // 根据出库时间范围查询
    List<TExpressOut> findByOutTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}