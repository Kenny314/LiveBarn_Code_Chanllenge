package com.kenny.challenge.controller;

import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.entity.view.Order;
import com.kenny.challenge.service.SushiOrderServiceInf;
import com.kenny.challenge.system.data.StatusData;
import com.kenny.util.DataTranlateUtil;
import com.kenny.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * A controller which is served POJO Sushi Order
 */
@RestController
public class SushiOrderController {

    @Autowired
    SushiOrderServiceInf sushiOrderService;


    /**
     * creat the Sushi order
     * @param sushi data gathered from front end
     * @param response set response code
     * @return ResultEntity<Order>
     * @throws InterruptedException
     */
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    @ResponseBody
//    @ResponseStatus(code = HttpStatus.CREATED)
    public ResultEntity<Order> createOrder(@RequestBody Sushi sushi, HttpServletResponse response) throws InterruptedException {
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


    /**
     * Using order_id to cancel an Order  and return result as json
     * @param sushiOrderId get orderId from front end
     * @return ResultEntity<SushiOrder>
     */
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value="/api/orders/{order_id}",method = RequestMethod.DELETE)
    public ResultEntity<SushiOrder> cancelOrder(@PathVariable("order_id") Long sushiOrderId){
        SushiOrder sushiOrder = sushiOrderService.getSushiOrder(sushiOrderId);
        if(sushiOrder == null){
            return ResultEntity.failed("can not find the order");
        } else {
            if(!sushiOrder.getStatus().getName().equals(StatusData.STATUS_PROCESS) || !sushiOrder.getStatus().getName().equals(StatusData.STATUS_CREAT)){
                return ResultEntity.failed("Order cancelled failed, cause order status is " + sushiOrder.getStatus().getName());
            } else {
                boolean result = sushiOrderService.cancelSushiOrder(sushiOrderId);
                if(result){
                    return ResultEntity.successWithoutData("Order cancelled successful");
                } else {
                    return ResultEntity.failed("Order cancelled failed.");
                }
            }

        }
    }

    /**
     * pause a order
     * only the order status is in-processed can be paused.
     * if the order status is not in-processed, return fail
     * @param sushiOrderId
     * order id whick is keep in database
     * @return ResultEntity<String>
     */
    @RequestMapping(value = "/api/orders/{order_id}/pause")
    public ResultEntity<String> pauseOrder(@PathVariable("order_id") Long sushiOrderId){
        boolean isOrderInprocess = this.sushiOrderService.getInprocessSushiOrder(sushiOrderId);
        if(!isOrderInprocess){
            return ResultEntity.failed("order is not existed");
        } else {
            boolean isPaused = this.sushiOrderService.pauseSushiOrder(sushiOrderId);
            if(isPaused){
                return ResultEntity.successWithoutData("Order paused successfully");
            } else {
                return ResultEntity.failed("order paused failed");
            }

        }
    }

    /**
     * only the order status is paused can be resumed;
     * if the status is not paused return failed
     * @param sushiOrderId orderId from URI
     * @return ResultEntity<String>
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
                this.sushiOrderService.resumeSushiOrder(sushiOrder.getId());
                return ResultEntity.successWithoutData("Order resumed successful");
            }
        }
    }

    /**
     * when chef is free ,Automatically making the order
     * @throws InterruptedException
     */
    @RequestMapping(value = "/api/orders/make")
    public void makeSushi() throws InterruptedException {
        int i = 10;
        while (true){
            this.sushiOrderService.processSushiOrder();
            Random r = new Random();
            int timeSleep = r.nextInt(i);
            Thread.sleep(timeSleep * 1000);
        }
    }


}
