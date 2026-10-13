package com.example.api.repository;

import com.example.api.model.entity.TExpressIn;
import com.example.api.model.entity.TExpressStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TExpressInRepository extends JpaRepository<TExpressIn, Long> {

    // 根据快递ID查询
    List<TExpressIn> findByExpressId(Long expressId);

    // 根据快递柜ID查询
    List<TExpressIn> findByCabinetId(Long cabinetId);

    // 根据状态查询
    List<TExpressIn> findByStatus(Integer status);

    // 根据入库操作人查询
    List<TExpressIn> findByInUserId(Long inUserId);

    // 根据验证码查询
    Optional<TExpressIn> findByVerifyCode(String verifyCode);

    // 根据入库类型查询
    List<TExpressIn> findByInType(Integer inType);

    // 查询过期未取的快递入库记录
    List<TExpressIn> findByExpireTimeBeforeAndStatus(LocalDateTime expireTime, Integer status);

    // 根据快递柜和柜口查询
    Optional<TExpressIn> findByCabinetIdAndBoxNo(Long cabinetId, String boxNo);

    Optional<TExpressIn> findByExpressIdAndStatus(Long expressId, Integer status);

}