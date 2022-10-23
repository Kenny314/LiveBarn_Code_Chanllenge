package com.kenny.challenge.service;

import com.kenny.challenge.entity.Sushi;

import java.util.List;

public interface SushiServiceInf {

    /**
     * return all sushi information defined in database
     * sushi is defined as a dictionary
     * @return List<Sushi>
     */
    List<Sushi> queryAllSushi();
}
