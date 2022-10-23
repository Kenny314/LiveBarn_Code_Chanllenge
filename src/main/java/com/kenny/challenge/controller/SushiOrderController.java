package com.kenny.challenge.controller;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.Order;
import com.kenny.challenge.service.impl.SushiOrderServiceImpl;
import com.kenny.challenge.system.data.StatusData;
import com.kenny.util.DataTranlateUtil;
import com.kenny.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SushiOrderController {

    @Autowired
    SushiOrderServiceImpl sushiOrderService;



    //TODO Only three chefs
    //Only three chefs are available, thus only three orders can be processed at the same time
    //When an order is submitted, the order record should be saved into database with status set to "created"
    //When a chef is ready to process an order, the corresponding order record should be updated in the database with status set to "in-progress"
    //The field "time_to_make" from sushi table represents how long it takes to make a specific kind of sushi. For example, a California Roll takes 30 seconds to make, thus a chef will be occupied for 30 seconds to finish making the sushi
    //When an order is completed, the corresponding order record should be updated in the database with status set to "finished"
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    @ResponseBody
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResultEntity<Order> createOrder(@RequestBody Sushi sushi, HttpServletResponse response){
        SushiOrder sushiOrder = this.sushiOrderService.creatOrderBySushiName(sushi.getName());
        if(sushiOrder != null){
            Order order = DataTranlateUtil.transSushiOrderToOrderView(sushiOrder);
            response.setStatus(HttpStatus.CREATED.value());
            return ResultEntity.successWithData(order,"Order created");
        } else {
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            return ResultEntity.failed("Create order failed");
        }

    }



    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value="/api/orders/{order_id}",method = RequestMethod.DELETE)
    public ResultEntity<SushiOrder> cancelOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("can not find the order");
        } else {

            Status cancelStatus = StatusData.getStatusData(StatusData.STATUS_CANCEL);
            sushiOrder.setStatus(cancelStatus);
            boolean result = sushiOrderService.updateSushiOrder(sushiOrder);
            if(result){
                return ResultEntity.successWithoutData("Order cancelled successful");
            } else {
                return ResultEntity.failed("Order cancelled failed");
            }
        }
    }

    /**
     * pause a order
     * only the order status is in-processed can be paused.
     * if the order status is not in-processed, return fail
     * @param sushiOrderId
     * @return
     */
    @RequestMapping(value = "/api/orders/{order_id}/pause")
    public ResultEntity<String> pauseOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = this.sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("order is not existed");
        } else {
            Status status = sushiOrder.getStatus();

            if(!status.getName().equals(StatusData.STATUS_PROCESS)){
                //return failed, if status is not "in-processed"
                return ResultEntity.failed("order's status is wrong, the status of order is "+ status.getName());
            } else {

                Status pauseStatus = StatusData.getStatusData(StatusData.STATUS_PAUSE);
                sushiOrder.setStatus(pauseStatus);
                this.sushiOrderService.updateSushiOrder(sushiOrder);
                return ResultEntity.successWithoutData("Order paused");
            }
        }
    }

    /**
     * only the order status is paused can be resumed;
     * if the status is not paused return failed
     * @param sushiOrderId
     * @return
     */
    @RequestMapping(value = "/api/orders/{order_id}/resume")
    public ResultEntity<String> resumeOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = this.sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("order is not existed");
        } else {
            Status status = sushiOrder.getStatus();

            if(!status.getName().equals(StatusData.STATUS_PAUSE)){
                return ResultEntity.failed("order's status is wrong, the status of order is "+ status.getName());
            } else {

                Status inprocessStatus = StatusData.getStatusData(StatusData.STATUS_PROCESS);
                sushiOrder.setStatus(inprocessStatus);
                this.sushiOrderService.updateSushiOrder(sushiOrder);
                return ResultEntity.successWithoutData("Order resumed successful");
            }
        }
    }
}
