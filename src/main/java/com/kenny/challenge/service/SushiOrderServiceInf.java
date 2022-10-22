package com.kenny.challenge.service;

import com.kenny.challenge.entity.SushiOrder;

public interface SushiOrderServiceInf {

//    public List<SushiOrder> querySushiOrderBySushiName(String sushiName);

    public SushiOrder creatOrderBySushiName(String sushiName);

    public boolean updateSushiOrder(SushiOrder sushiOrder);

    public SushiOrder getSushiOrder(Long sushiOrderId);
}
