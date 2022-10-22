package com.kenny.challenge.service.impl;

import com.kenny.challenge.service.SushiOrderServiceInf;
import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.repo.SushiOrderRepository;
import com.kenny.challenge.repo.SushiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Sushi sushi = this.sushiRepository.findSushiByName(sushiName);

        if(sushi != null){
            SushiOrder sushiOrder = new SushiOrder();
            //TODO 优化 status
            Status status = new Status("aaa");
            sushiOrder.setSushi(sushi);
            sushiOrder.setStatus(status);
            this.sushiOrderRepository.save(sushiOrder);
            return sushiOrder;
        } else {
            return null;
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
