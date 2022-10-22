package com.kenny.challenge.system.data;

import com.kenny.challenge.service.impl.StatusServiceImpl;
import com.kenny.challenge.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatusData {
    private static  Map<String,Status> STATUS_DATA = new HashMap<>();
    private static StatusData statusData;

    @Autowired
    private StatusServiceImpl statusService;

    @PostConstruct
    public void initStatusDataToMemory(){
        List<Status> statusList = this.statusService.queryAllStatus();

        if(statusList.size() != 0){
            for(Status status : statusList){
                STATUS_DATA.put(status.getName(), status);
            }
        }
    }

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
