package com.kenny.challenge.entity.view;

import java.util.List;

public class StatusOrderView {
    private String orderStatus;
    private List<OrderTimespend> orderTimespends;

    public StatusOrderView(String orderStatus, List<OrderTimespend> orderTimespends) {
        this.orderStatus = orderStatus;
        this.orderTimespends = orderTimespends;
    }

    public StatusOrderView() {

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

    public void setOrderTimespends(List<OrderTimespend> orderTimespends) {
        this.orderTimespends = orderTimespends;
    }
}
