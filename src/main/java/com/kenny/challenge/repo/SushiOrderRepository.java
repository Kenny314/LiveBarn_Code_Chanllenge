package com.kenny.challenge.repo;

import com.kenny.challenge.entity.SushiOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SushiOrderRepository extends JpaRepository<SushiOrder,Long> {

//    @Query(value = "select * from sushi_order order where order.sushi_id = (" +
//            "select id from sushi su where su.name like :name)",nativeQuery = true)
//    public List<SushiOrder> findSushiOrdersBySushiName(@Param("name") String sushiName);
//
//

}
