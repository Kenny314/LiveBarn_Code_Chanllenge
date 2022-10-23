package com.kenny.challenge.service;

import com.kenny.challenge.entity.SushiOrder;

/**
 * Service Layer, which is served Order
 */
public interface SushiOrderServiceInf {

    /**
     * create order by sushi name
     * @param sushiName
     * @return SushiOrder
     */
    SushiOrder creatOrderBySushiName(String sushiName);

    /**
     * update order information
     *
     * @param sushiOrder,
     * in the param sushiOrder, id is the order id want to update, other information is
     * @return boolean
     */
    boolean updateSushiOrder(SushiOrder sushiOrder);

    /**
     * update order status to cancel
     *
     * @param orderId
     * @return boolean
     */
    boolean cancelSushiOrder(Long orderId);

    /**
     * update order status to pause
     * @param orderId
     * @return boolean
     */
    boolean pauseSushiOrder(Long orderId);

    /**
     *
     * update a order status to in process
     * @param orderId
     * @return boolean
     */
    boolean resumeSushiOrder(Long orderId);
    /**
     * get order Information by order id
     * @param sushiOrderId
     * @return
     */
    SushiOrder getSushiOrder(Long sushiOrderId);
}
