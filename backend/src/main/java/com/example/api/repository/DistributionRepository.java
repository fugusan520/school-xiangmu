package com.example.api.repository;

import com.example.api.model.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, String> {

    @Query("SELECT d FROM Distribution d WHERE d.vid = :vid")
    List<Distribution> findAllByVehicleId(@Param("vid") String vid);
}
