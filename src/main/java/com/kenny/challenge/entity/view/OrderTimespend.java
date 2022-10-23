package com.kenny.challenge.entity.view;

public class OrderTimespend {
    private Long orderId;
    private String timeSpend;

    public OrderTimespend(Long orderId, String timeSpend) {
        this.orderId = orderId;
        this.timeSpend = timeSpend;
    }

    public OrderTimespend() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(String timeSpend) {
        this.timeSpend = timeSpend;
    }

    @Override
    public String toString() {
        return "\r\t  {" + "\r"+
                "\t\torderId:" + orderId + ",\r"+
                "\t\ttimeSpend:'" + timeSpend + "\r" +
                "\t  }";
    }
}
