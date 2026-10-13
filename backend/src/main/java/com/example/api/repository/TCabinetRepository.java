package com.example.api.repository;

import com.example.api.model.entity.TCabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TCabinetRepository extends JpaRepository<TCabinet, Long> {

    // 根据快递柜编号查询
    Optional<TCabinet> findByCabinetNo(String cabinetNo);

    // 根据状态查询
    List<TCabinet> findByStatus(Integer status);

    // 根据位置模糊查询
    List<TCabinet> findByLocationContaining(String location);

    // 检查快递柜编号是否存在
    boolean existsByCabinetNo(String cabinetNo);

    // 根据快递柜名称模糊查询
    List<TCabinet> findByCabinetNameContaining(String cabinetName);
}