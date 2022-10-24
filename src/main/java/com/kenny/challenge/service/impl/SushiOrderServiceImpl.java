package com.kenny.challenge.service.impl;

import com.kenny.challenge.entity.OrderHistory;
import com.kenny.challenge.repo.OrderHistoryRepository;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class SushiOrderServiceImpl implements SushiOrderServiceInf {

    private static final Logger logger = LoggerFactory.getLogger(SushiOrderServiceImpl.class);

    /**
     * 1.use this linkedBlockingDqueue to keep the orders which are created successful
     * and the chef can get the order from this structure with FIFO
     * 2.when a paused order resumed, we can put the resumed order in the first of queue,
     * when a chef is available the order will be starting cooking again
     * 3.initial size is 30
     */
    private LinkedBlockingDeque<SushiOrder> orderWaitingProcessQueue = new LinkedBlockingDeque(30);

    /**
     * 1.when a order is in processing, the chef is blocked. But the order can be paused,
     * so when the command pausing an order is received, we can call the chef unblock.
     * <p>
     * 2. this structure keep Key:orderId, value:thread which is binding to the order is block.
     * <p>
     * 3.when the order finished or paused, remove the key-value from map
     */
    private ConcurrentHashMap<Long, Thread> inprocessOrderThreadMap = new ConcurrentHashMap<>();


    @Autowired
    SushiOrderRepository sushiOrderRepository;

    @Autowired
    SushiRepository sushiRepository;

    @Autowired
    OrderHistoryRepository orderHistoryRepository;


    @Resource
    private TransactionTemplate transactionTemplate;


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


    @Override
    public boolean pauseSushiOrder(Long orderId) {
        logger.info("pause order id :" + orderId);
        if (inprocessOrderThreadMap.containsKey(orderId)) {
            Thread thread = this.inprocessOrderThreadMap.get(orderId);
            thread.interrupt();
            return true;
        }
        return false;

    }

    @Transactional
    @Override
    public boolean resumeSushiOrder(Long orderId) {
        Optional<SushiOrder> option = sushiOrderRepository.findById(orderId);
        if (option.isEmpty()) {
            return false;
        } else {
            SushiOrder sushiOrder = option.get();
            Status resumeStatus = StatusData.getStatusData(StatusData.STATUS_CREAT);
            sushiOrder.setStatus(resumeStatus);
            /*
            a resumed order should have high priority, it means when a chef is available, this order should starting cooking again
             */
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
    @Override
    @Async
    public void processSushiOrder() throws InterruptedException {
        /*
        Get the first order from the FIFO queue
         */
        SushiOrder sushiOrder = this.orderWaitingProcessQueue.poll();
        if (sushiOrder != null && sushiOrder.getStatus().getName().equals(StatusData.STATUS_CREAT)) {
            transactionTemplate.execute(status -> {
                logger.info("====" + Thread.currentThread().getName() + "=== Order_id = " + sushiOrder.getId());
                SushiOrder finalSushiOrder = this.sushiOrderRepository.getReferenceById(sushiOrder.getId());
                Status startStatus = StatusData.getStatusData(StatusData.STATUS_PROCESS);
                finalSushiOrder.setStatus(startStatus);
                //save order history info
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrder(finalSushiOrder);
                orderHistory.setStatus(startStatus);
                orderHistory.setCreateAt(new Timestamp(System.currentTimeMillis()));
                this.orderHistoryRepository.save(orderHistory);
                return this.sushiOrderRepository.saveAndFlush(finalSushiOrder);

            });
            //based on the how long to finish making the sushi , the thread sleep
            //calculate order making time

            Long resumeMakingTime = this.calAlreadyMakeTime(sushiOrder);
            logger.info("from inprocess to resume working time : " + resumeMakingTime);
            Long sleepTime = 0L;
            if(resumeMakingTime == 0){
                sleepTime = Long.valueOf(sushiOrder.getSushi().getTimeToMake() * 1000);
            } else {
                sleepTime = Long.valueOf(sushiOrder.getSushi().getTimeToMake() * 1000) - resumeMakingTime;
                logger.info("the rest 0f working time : "+sleepTime);
            }


            this.inprocessOrderThreadMap.put(sushiOrder.getId(), Thread.currentThread());
            //making sushi
            try {

                Thread.sleep(sleepTime);
                Status finishStatus = StatusData.getStatusData(StatusData.STATUS_FINISH);
                sushiOrder.setStatus(finishStatus);
                this.sushiOrderRepository.saveAndFlush(sushiOrder);
                this.inprocessOrderThreadMap.remove(sushiOrder.getId());
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrder(sushiOrder);
                orderHistory.setStatus(StatusData.getStatusData(StatusData.STATUS_FINISH));
                orderHistory.setCreateAt(new Timestamp(System.currentTimeMillis()));
                this.orderHistoryRepository.save(orderHistory);
            } catch (InterruptedException e) {
                logger.info("Thread name : " + Thread.currentThread().getName() + " ===== Order_id :" + sushiOrder.getId());
                Status pauseStatus = StatusData.getStatusData(StatusData.STATUS_PAUSE);
                sushiOrder.setStatus(pauseStatus);
                this.sushiOrderRepository.saveAndFlush(sushiOrder);
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrder(sushiOrder);
                orderHistory.setStatus(pauseStatus);
                orderHistory.setCreateAt(new Timestamp(System.currentTimeMillis()));
                this.orderHistoryRepository.save(orderHistory);
                this.inprocessOrderThreadMap.remove(sushiOrder.getId());
                return;
            }

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


    /**
     * get in process order from map
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean getInprocessSushiOrder(Long orderId) {
        return inprocessOrderThreadMap.containsKey(orderId);
    }

    private Long calAlreadyMakeTime(SushiOrder sushiOrder){
        List<OrderHistory> orderHistoryList = this.orderHistoryRepository.queryHistoryByOrderId(sushiOrder.getId());
        if(orderHistoryList == null || orderHistoryList.size() == 0){
            return 0L;
        } else {
            int orderSize = orderHistoryList.size();
            int loopTime = 0;
            if(orderSize % 2 == 0){
                loopTime = orderSize;
            } else {
                loopTime = orderSize - 1;
            }
            Long result = 0L;
            for(int i = 0; i < loopTime ;i++){
                OrderHistory orderHistory = orderHistoryList.get(i);
                if(i % 2 == 0){
                    result = result - orderHistory.getCreateAt().getTime();
                } else {
                    result = result + orderHistory.getCreateAt().getTime();
                }
            }
            return result;
        }
    }


}
