package com.kenny.util;

import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.Order;

public class DataTranlateUtil {
    public static Order transSushiOrderToOrderView(SushiOrder sushiOrder){
        if(sushiOrder == null){
            return null;
        } else {
            Order order = new Order();
            order.setId(sushiOrder.getId());
            order.setSushiId(sushiOrder.getSushi().getId());
            order.setStatusId(sushiOrder.getStatus().getId());
            order.setCreatedAt(sushiOrder.getCreatedAt().getTime());
            return order;
        }
    }
}
