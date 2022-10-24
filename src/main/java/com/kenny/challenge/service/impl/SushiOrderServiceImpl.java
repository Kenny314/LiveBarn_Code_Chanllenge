package com.kenny.challenge.service.impl;

import com.kenny.challenge.service.SushiOrderServiceInf;
import com.kenny.challenge.entity.Status;
import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.entity.SushiOrder;
import com.kenny.challenge.repo.SushiOrderRepository;
import com.kenny.challenge.repo.SushiRepository;
import com.kenny.challenge.system.data.StatusData;
import com.kenny.challenge.system.data.SushiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class SushiOrderServiceImpl implements SushiOrderServiceInf {

    private static final Logger logger = LoggerFactory.getLogger(SushiOrderServiceImpl.class);

    /*
    keep orders which status is created
     */
    private LinkedBlockingDeque<SushiOrder> orderWaitingProcessQueue = new LinkedBlockingDeque(30);

    /*
    keep orders which status is in processing
     */
    private ConcurrentHashMap<Long,Thread> inprocessOrderThreadMap = new ConcurrentHashMap<>();

    /*
     * keep orders which status is paused
     */
    private ConcurrentHashMap<Long,Thread> pauseedOrderThreadMap = new ConcurrentHashMap<>();

    @Autowired
    SushiOrderRepository sushiOrderRepository;

    @Autowired
    SushiRepository sushiRepository;


    public SushiOrderRepository getSushiOrderRepository() {
        return sushiOrderRepository;
    }



    @Transactional
    public SushiOrder creatOrderBySushiName(String sushiName) throws InterruptedException {
        logger.info(sushiName);
        Sushi sushi = SushiData.getSushiData(sushiName);
        if (sushi != null) {
            SushiOrder sushiOrder = new SushiOrder();
            Status createStatus = StatusData.getStatusData(StatusData.STATUS_CREAT);
            sushiOrder.setSushi(sushi);
            sushiOrder.setStatus(createStatus);
            sushiOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            this.sushiOrderRepository.save(sushiOrder);
            // put in thread safe structure
            //TODO if in a real enviroment, waiting list order should keep in MQ structure, when we restart the system the queue don't lost.
            this.orderWaitingProcessQueue.put(sushiOrder);
            return sushiOrder;
        } else {
            return null;
        }
    }


    @Transactional
    @Override
    public boolean cancelSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if (option.isEmpty()) {
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            logger.info("sushiOrder status :" + sushiOrder.getStatus().getName());
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
        if (option.isEmpty()) {
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Thread thread = this.inprocessOrderThreadMap.get(orderId);
            sushiOrder = this.sushiOrderRepository.getReferenceById(orderId);
            Status pauseStatus = StatusData.getStatusData(StatusData.STATUS_PAUSE);
            sushiOrder.setStatus(pauseStatus);
            if(!Thread.currentThread().interrupted()){
                Thread.currentThread().interrupt();
            }
            this.sushiOrderRepository.saveAndFlush(sushiOrder);
            //remove the inprocess thread
            inprocessOrderThreadMap.remove(orderId);
            logger.info("&&&&&&&&&& thread name = " +thread.getName() );
            //interrupt in processing order

            return true;
        }
    }

    @Transactional
    @Override
    public boolean resumeSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if (option.isEmpty()) {
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Status resumeStatus = StatusData.getStatusData(StatusData.STATUS_PROCESS);
            sushiOrder.setStatus(resumeStatus);
            this.orderWaitingProcessQueue.addFirst(sushiOrder);
            this.sushiOrderRepository.save(sushiOrder);
            return true;
        }
    }

    /**
     * start to make the sushi
     *
     * @param
     * @return
     */
    @Transactional
    @Override
    @Async
    public void processSushiOrder() throws InterruptedException {

        SushiOrder sushiOrder = this.orderWaitingProcessQueue.poll();

        if (sushiOrder!= null && sushiOrder.getStatus().getName().equals(StatusData.STATUS_CREAT)) {
            logger.info("====" + Thread.currentThread().getName() + "=== Order_id = " + sushiOrder.getId());
            sushiOrder = this.sushiOrderRepository.getById(sushiOrder.getId());
            Status startStatus = StatusData.getStatusData(StatusData.STATUS_PROCESS);
            sushiOrder.setStatus(startStatus);
            logger.info("*************** starting *************");
            this.sushiOrderRepository.save(sushiOrder);
            //based on the how long to finish making the sushi , the thread sleep
            Long sleepTime = Long.valueOf(sushiOrder.getSushi().getTimeToMake() * 1000);
            this.inprocessOrderThreadMap.put(sushiOrder.getId(),Thread.currentThread());
            //making sushi
            Thread.sleep(sleepTime);

            Status finishStatus = StatusData.getStatusData(StatusData.STATUS_FINISH);
            sushiOrder.setStatus(finishStatus);
            this.sushiOrderRepository.save(sushiOrder);
            this.inprocessOrderThreadMap.remove(sushiOrder.getId());
        }

    }

    @Transactional
    public boolean updateSushiOrder(SushiOrder sushiOrder) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(sushiOrder.getId());
        if (option.isEmpty()) {
            return false;
        } else {
            SushiOrder sushiOrder1 = option.get();
            sushiOrder1.setSushi(sushiOrder.getSushi());
            sushiOrder1.setStatus(sushiOrder.getStatus());
            this.sushiOrderRepository.save(sushiOrder1);
            return true;
        }
    }

    public SushiOrder getSushiOrder(Long sushiOrderId) {
        Optional<SushiOrder> optional = this.sushiOrderRepository.findById(sushiOrderId);
        if (optional.isEmpty()) {
            return null;
        } else {
            return optional.get();
        }
    }

    @Override
    public List<SushiOrder> getCreatedSushiOrders() {
        Status status = StatusData.getStatusData(StatusData.STATUS_CREAT);
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<SushiOrder> sushiOrders = this.sushiOrderRepository.findByStatus(status, sort);
        return sushiOrders;
    }


    /**
     * get in process order from map
     * @param orderId
     * @return
     */
    @Override
    public boolean getInprocessSushiOrder(Long orderId) {
        return inprocessOrderThreadMap.containsKey(orderId);
    }


}
