package com.kenny.challenge.service.impl;

import com.kenny.challenge.repo.SushiRepository;
import com.kenny.challenge.service.SushiServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SushiServiceImpl implements SushiServiceInf {

    @Autowired
    SushiRepository sushiRepository;


}
