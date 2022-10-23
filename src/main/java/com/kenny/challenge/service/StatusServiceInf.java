package com.kenny.challenge.service;

import com.kenny.challenge.entity.Status;

import java.util.List;

public interface StatusServiceInf {

    /**
     * return all status information defined in database
     * status is defined as a dictionary
     * @return List<Status>
     */
    List<Status> queryAllStatus();
}
