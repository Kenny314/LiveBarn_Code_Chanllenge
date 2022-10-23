package com.kenny.challenge.entity.view;

import java.util.ArrayList;
import java.util.List;

/**
 * show all orders information and grouped by the order status
 */
public class StatusOrderView {
    private String orderStatus;
    private List<OrderTimespend> orderTimespends;

    public StatusOrderView(String orderStatus, List<OrderTimespend> orderTimespends) {
        this.orderStatus = orderStatus;
        this.orderTimespends = orderTimespends;
    }

    public StatusOrderView() {
        this.orderTimespends = new ArrayList<>();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderTimespend> getOrderTimespends() {
        return orderTimespends;
    }

    @Override
    public String toString() {
        StringBuilder sborderTimespends = new StringBuilder();
        if(orderTimespends != null && orderTimespends.size() != 0){
            int i = 0;
            for(OrderTimespend orderTimespend: orderTimespends){
                sborderTimespends.append(orderTimespend.toString());
                if(i == orderTimespends.size() -1 ){
                    sborderTimespends.append("");

                } else {
                    sborderTimespends.append(",");
                    i++;
                }

            }
        }
        String result = sborderTimespends.toString();


        return result;
    }
}
