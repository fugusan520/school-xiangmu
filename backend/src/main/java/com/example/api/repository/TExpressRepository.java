package com.example.api.repository;

import com.example.api.model.entity.TExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TExpressRepository extends JpaRepository<TExpress, Long> {

    // 根据快递单号查询
    Optional<TExpress> findByExpressNo(String expressNo);

    // 根据状态查询
    List<TExpress> findByStatus(Integer status);

    // 根据快递公司编码查询
    List<TExpress> findByCompanyCode(String companyCode);

    // 根据收件人电话查询
    List<TExpress> findByReceiverPhone(String receiverPhone);

    // 根据寄件人ID查询
    List<TExpress> findByCreateUserId(Long createUserId);

    // 根据工作人员ID查询
    List<TExpress> findByCourierId(Long courierId);

    // 检查快递单号是否存在
    boolean existsByExpressNo(String expressNo);

    // 根据业务类型查询
    List<TExpress> findByBizType(Integer bizType);
}