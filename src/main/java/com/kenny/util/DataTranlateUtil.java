package com.kenny.util;

import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.Order;
import com.kenny.challenge.entity.view.StatusOrderView;

import java.util.List;


public class DataTranlateUtil {

    /**
     * format Order information to order view
     * @param sushiOrder
     * @return
     */
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

    /**
     * return all order information and format as the requirement
     * @param statusOrderViewList
     * @return String
     */
    public static String transStatusOrderViewListToString(List<StatusOrderView> statusOrderViewList){
        StringBuilder result = new StringBuilder("{\r");
        int i = 0;
        for(StatusOrderView statusOrderView:statusOrderViewList){
            result.append("\t");
            result.append(statusOrderView.getOrderStatus() + ":[" );
            if(i < statusOrderViewList.size() -1 ){
                result.append("\r\t],\r");
                i++;
            } else {
                result.append("\r\t]\r");
            }
        }
        result.append("\r}");
        return result.toString();
    }
}
