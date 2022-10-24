package com.kenny.challenge.repo;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.SushiOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order DAO
 */
@Repository
public interface SushiOrderRepository extends JpaRepository<SushiOrder,Long> {

}