package com.kenny.challenge.repo;

import com.kenny.challenge.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {
    /**
     * repository to manipulate OrderHistory
     * @param orderId the key of order Id
     * @return List<OrderHistory>
     */
    @Query(value = "select * from order_history where order_id=?1 and status_id in (2,3) order by id asc ",nativeQuery=true)
    List<OrderHistory> queryHistoryByOrderId(Long orderId);
}
