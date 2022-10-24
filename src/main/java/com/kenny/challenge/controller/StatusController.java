package com.kenny.challenge.controller;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.OrderTimespend;
import com.kenny.challenge.entity.view.StatusOrderView;
import com.kenny.challenge.service.StatusServiceInf;
import com.kenny.challenge.system.data.StatusData;
import com.kenny.util.DataTranlateUtil;
import com.kenny.util.ResultEntity;
import com.kenny.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A controller which is served POJO Status
 */
@RestController
public class StatusController {

    @Autowired
    private StatusServiceInf statusService;

    /**
     * retrieved all orders by order-status
     * StatusOrderView is a view object which can organized the data and make data format in order to display better to frontend
     *
     * @return ResultEntity
     */
    @GetMapping(value = "/api/orders/status")
    public Object getStatusOrderInfo() {
        List<Status> statusList = statusService.queryAllStatus();
        List<StatusOrderView> statusOrderViewList = new ArrayList<>();
        if (statusList == null || statusList.size() == 0) {
            return ResultEntity.failed("get order status is wrong");
        } else {
            for (Status status : statusList) {
                StatusOrderView statusOrderView = new StatusOrderView();
                statusOrderView.setOrderStatus(status.getName());

                for (SushiOrder sushiOrder : status.getSushiOrders()) {
                    OrderTimespend orderTimespend = new OrderTimespend();
                    orderTimespend.setOrderId(sushiOrder.getId());
                    //TODO confirm timespent definition
                    orderTimespend.setTimeSpend(sushiOrder.getCreatedAt().toString());
                    if (sushiOrder.getStatus().getName().equals(StatusData.STATUS_FINISH)) {
                        orderTimespend.setTimeSpend(sushiOrder.getSushi().getTimeToMake().toString());
                    } else {
                        Long timeSp =  TimeUtil.calTimeDifference(new Timestamp(System.currentTimeMillis()),sushiOrder.getCreatedAt());
                        orderTimespend.setTimeSpend(timeSp.toString());
                    }
                    statusOrderView.getOrderTimespends().add(orderTimespend);
                }
                statusOrderViewList.add(statusOrderView);
            }
            // format the information
            return DataTranlateUtil.transStatusOrderViewListToString(statusOrderViewList);
        }
    }
}
