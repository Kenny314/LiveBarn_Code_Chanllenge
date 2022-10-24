package com.kenny.challenge.service.impl;

import com.kenny.challenge.service.StatusServiceInf;
import com.kenny.challenge.entity.Status;
import com.kenny.challenge.repo.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusServiceInf {

    @Autowired
    StatusRepository statusRepository;

    /**
     * return all status information defined in database
     * status is defined as a dictionary
     * @return List<Status>
     */
    @Override
    public List<Status> queryAllStatus() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        return statusRepository.findAll(sort);
    }

}
