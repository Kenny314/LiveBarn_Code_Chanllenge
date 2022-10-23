package com.kenny.challenge.service.impl;

import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.repo.SushiRepository;
import com.kenny.challenge.service.SushiServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SushiServiceImpl implements SushiServiceInf {

    @Autowired
    SushiRepository sushiRepository;


    @Override
    public List<Sushi> queryAllSushi() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        List<Sushi> sushiList = this.sushiRepository.findAll(sort);

        return sushiList;
    }
}
