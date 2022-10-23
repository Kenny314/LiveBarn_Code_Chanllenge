package com.kenny.challenge.system.data;

import com.kenny.challenge.service.impl.StatusServiceImpl;
import com.kenny.challenge.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Order status dictionary
 * put all order status in static map and provide api to get status
 * in product-enviroment is better to update to nosql db
 */

//TODO need a mechanism to update map. 1. when a new status saved or status name update reload 2. provide a function can reload manual
@Component
public class StatusData {


    public static final String STATUS_CANCEL = "cancelled";
    public static final String STATUS_CREAT = "created";
    public static final String STATUS_PROCESS = "in-progress";
    public static final String STATUS_PAUSE = "paused";
    public static final String STATUS_FINISH = "finished";
    private static  Map<String,Status> STATUS_DATA = new HashMap<>();
    private static StatusData statusData;

    @Autowired
    private StatusServiceImpl statusService;


    /**
     * loading order status into map from db
     */
    @PostConstruct
    public void initStatusDataToMemory(){
        List<Status> statusList = this.statusService.queryAllStatus();

        if(statusList.size() != 0){
            for(Status status : statusList){
                STATUS_DATA.put(status.getName(), status);
            }


        }
    }

    /**
     * using status name to get Status from the map.
     * @param statusName
     * @return
     */
    public static Status getStatusData(String statusName){
        return STATUS_DATA.get(statusName);

    }

    public StatusData(StatusServiceImpl statusService) {
        this.statusService = statusService;
    }

    public StatusData() {
    }

    public StatusServiceImpl getStatusService() {
        return statusService;
    }

    public void setStatusService(StatusServiceImpl statusService) {
        this.statusService = statusService;
    }
}
