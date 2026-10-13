package com.example.api.repository;

import com.example.api.model.entity.TExpressStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TExpressStockRepository extends JpaRepository<TExpressStock, Long> {

    // 根据快递ID查询
    Optional<TExpressStock> findByExpressId(Long expressId);

    // 根据状态查询
    List<TExpressStock> findByStatus(Integer status);

    // 根据快递柜ID查询
    List<TExpressStock> findByCabinetId(Long cabinetId);

    // 根据快递柜和柜口查询
    Optional<TExpressStock> findByCabinetIdAndBoxNo(Long cabinetId, String boxNo);

    // 根据快递柜、柜口和状态查询
    Optional<TExpressStock> findByCabinetIdAndBoxNoAndStatus(Long cabinetId, String boxNo, Integer status);

    // 查询快递柜中的快递数量
    long countByCabinetIdAndStatus(Long cabinetId, Integer status);

    Optional<TExpressStock> findByExpressIdAndStatus(Long expressId, Integer status);

}