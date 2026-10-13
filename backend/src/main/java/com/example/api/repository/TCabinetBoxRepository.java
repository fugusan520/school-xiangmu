package com.example.api.repository;

import com.example.api.model.entity.TCabinetBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TCabinetBoxRepository extends JpaRepository<TCabinetBox, Long> {

    // 根据快递柜ID查询
    List<TCabinetBox> findByCabinetId(Long cabinetId);
    List<TCabinetBox> findByCabinetIdIn(List<Long> cabinetIds);

    // 根据状态查询
    List<TCabinetBox> findByStatus(Integer status);

    // 根据柜口类型查询
    List<TCabinetBox> findByBoxType(Integer boxType);

    // 根据快递柜ID和柜口类型查询
    List<TCabinetBox> findByCabinetIdAndBoxType(Long cabinetId, Integer boxType);

    // 根据快递柜ID、柜口编号查询
    Optional<TCabinetBox> findByCabinetIdAndBoxNo(Long cabinetId, String boxNo);

    // 查询快递柜的空闲柜口数量
    long countByCabinetIdAndStatus(Long cabinetId, Integer status);

    // 查询快递柜的故障柜口
    List<TCabinetBox> findByCabinetIdAndStatus(Long cabinetId, Integer status);


    Optional<TCabinetBox> findFirstByStatus(Integer status);

}