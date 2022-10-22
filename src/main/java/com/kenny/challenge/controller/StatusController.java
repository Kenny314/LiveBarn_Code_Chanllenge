package com.kenny.challenge.controller;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.OrderTimespend;
import com.kenny.challenge.entity.view.StatusOrderView;
import com.kenny.challenge.service.impl.StatusServiceImpl;
import com.kenny.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatusController {

    @Autowired
    private StatusServiceImpl statusService;

    @GetMapping(value="/api/orders/status")
    public ResultEntity<List<StatusOrderView>> getStatusOrderInfo(){
        List<Status> statusList = statusService.queryAllStatus();
        List<StatusOrderView> statusOrderViewList = new ArrayList<>();
        if(statusList == null || statusList.size() == 0){
            return ResultEntity.failed("get order status is wrong");
        } else {
            for(Status status:statusList){
                StatusOrderView statusOrderView = new StatusOrderView();
                statusOrderView.setOrderStatus(status.getName());
                List<OrderTimespend> orderTimespendList = new ArrayList<>();
                for(SushiOrder sushiOrder : status.getSushiOrders()){
                    OrderTimespend orderTimespend = new OrderTimespend();
                    orderTimespend.setOrderId(sushiOrder.getId());
                    //TODO 确认timespent 的定义
                    orderTimespend.setTimeSpend(sushiOrder.getCreatedAt().toString());
                    orderTimespendList.add(orderTimespend);
                }
                statusOrderViewList.add(statusOrderView);
            }
            return ResultEntity.successWithData(statusOrderViewList);
        }
    }
}
