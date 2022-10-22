package com.kenny.challenge.controller;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.service.impl.SushiOrderServiceImpl;
import com.kenny.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class SushiOrderController {

    @Autowired
    SushiOrderServiceImpl sushiOrderService;


    @RequestMapping(value = "/api/orders/name/{sushiName}", method = RequestMethod.GET)
    @ResponseBody
    public ResultEntity<SushiOrder> createOrder(@PathVariable("sushiName") String sushiName){
        SushiOrder sushiOrder = this.sushiOrderService.creatOrderBySushiName(sushiName);
        if(sushiOrder != null){
            return ResultEntity.successWithData(sushiOrder);
        } else {
            return ResultEntity.failed("create order failed");
        }

    }



    @RequestMapping(value="/api/orders/{order_id}",method = RequestMethod.GET)
    public ResultEntity<SushiOrder> cancelOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("can not find the order");
        } else {
            //TODO get cancel status
            Status status = new Status("bbb");
            sushiOrder.setStatus(status);
            boolean result = sushiOrderService.updateSushiOrder(sushiOrder);
            if(result){
                return ResultEntity.successWithoutData("Order cancelled");
            } else {
                return ResultEntity.failed("Order cancelled failed");
            }
        }
    }

    @RequestMapping(value = "/api/orders/{order_id}/pause")
    public ResultEntity<String> pauseOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = this.sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("order is not existed");
        } else {
            Status status = sushiOrder.getStatus();
            //TODO 判断状态
            if(!status.getName().equals("in-progress")){
                //只要不为inproess 就报错
                return ResultEntity.failed("order's status is wrong, the status of order is "+ status.getName());
            } else {
                //TODO 获取inprocess status
                Status inprocessStatus = new Status("pause");
                sushiOrder.setStatus(inprocessStatus);
                this.sushiOrderService.updateSushiOrder(sushiOrder);
                return ResultEntity.successWithoutData("Order paused");
            }
        }
    }

    @RequestMapping(value = "/api/orders/{order_id}/resume")
    public ResultEntity<String> resumeOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = this.sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("order is not existed");
        } else {
            Status status = sushiOrder.getStatus();
            //TODO
            if(!status.getName().equals("paused")){
                return ResultEntity.failed("order's status is wrong, the status of order is "+ status.getName());
            } else {
                //TODO 获取inprocess status
                Status inprocessStatus = new Status("in-progress");
                sushiOrder.setStatus(inprocessStatus);
                this.sushiOrderService.updateSushiOrder(sushiOrder);
                return ResultEntity.successWithoutData("Order is in-process");
            }
        }
    }
}
