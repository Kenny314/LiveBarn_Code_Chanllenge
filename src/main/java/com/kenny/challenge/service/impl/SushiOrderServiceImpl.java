package com.kenny.challenge.service.impl;

import com.kenny.challenge.service.SushiOrderServiceInf;
import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.repo.SushiOrderRepository;
import com.kenny.challenge.repo.SushiRepository;
import com.kenny.challenge.system.data.StatusData;
import com.kenny.challenge.system.data.SushiData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class SushiOrderServiceImpl implements SushiOrderServiceInf {

    @Autowired
    SushiOrderRepository sushiOrderRepository;

    @Autowired
    SushiRepository sushiRepository;

    public SushiOrderRepository getSushiOrderRepository() {
        return sushiOrderRepository;
    }


//    @Override
//    @Transactional
//    public List<SushiOrder> querySushiOrderBySushiName(String sushiName) {
//        List<SushiOrder> result = sushiOrderRepository.findSushiOrdersBySushiName(sushiName);
//        return result;
//    }

    @Transactional
    public SushiOrder creatOrderBySushiName(String sushiName){
//        Sushi sushi = this.sushiRepository.findSushiByName(sushiName);
        Sushi sushi = SushiData.getSushiData(sushiName);
        if(sushi != null){
            SushiOrder sushiOrder = new SushiOrder();

            Status createStatus = StatusData.getStatusData(StatusData.STATUS_CREAT);
            sushiOrder.setSushi(sushi);
            sushiOrder.setStatus(createStatus);
            sushiOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            this.sushiOrderRepository.save(sushiOrder);
            return sushiOrder;
        } else {
            return null;
        }
    }


    @Transactional
    @Override
    public boolean cancelSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if(option.isEmpty()){
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Status cancleStatus = StatusData.getStatusData(StatusData.STATUS_CANCEL);
            sushiOrder.setStatus(cancleStatus);
            this.sushiOrderRepository.save(sushiOrder);
            return true;
        }
    }

    @Transactional
    @Override
    public boolean pauseSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if(option.isEmpty()){
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Status pauseStatus = StatusData.getStatusData(StatusData.STATUS_PAUSE);
            sushiOrder.setStatus(pauseStatus);
            this.sushiOrderRepository.save(sushiOrder);
            return true;
        }
    }

    @Transactional
    @Override
    public boolean resumeSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if(option.isEmpty()){
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Status resumeStatus = StatusData.getStatusData(StatusData.STATUS_PROCESS);
            sushiOrder.setStatus(resumeStatus);
            this.sushiOrderRepository.save(sushiOrder);
            return true;
        }
    }

    @Transactional
    public boolean updateSushiOrder(SushiOrder sushiOrder){
        Optional<SushiOrder> option = sushiOrderRepository.findById(sushiOrder.getId());
        if(option.isEmpty()){
            return false;
        } else {
            SushiOrder sushiOrder1 = option.get();
            sushiOrder1.setSushi(sushiOrder.getSushi());
            sushiOrder1.setStatus(sushiOrder.getStatus());
            this.sushiOrderRepository.save(sushiOrder1);
            return true;
        }
    }

    public SushiOrder getSushiOrder(Long sushiOrderId){
        Optional<SushiOrder> optional = this.sushiOrderRepository.findById(sushiOrderId);
        if(optional.isEmpty()){
            return null;
        } else {
            return optional.get();
        }
    }


}
