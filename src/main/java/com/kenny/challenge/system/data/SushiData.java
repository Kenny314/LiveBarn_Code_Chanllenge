package com.kenny.challenge.system.data;

import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.service.impl.SushiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * SUSHI is a dictionary , including what type of sushi can made.
 * so load sushi in map
 * in product-enviroment is better to update to nosql db
 */
//TODO need a mechanism to update map. 1. when a new status saved or status name update reload 2. provide a function can reload manual
@Component
public class SushiData {
   
    public static final String SUSHI_CAL_ROLL="California Roll";
    public static final String SUSHI_KAM_ROLL="Kamikaze Roll";
    public static final String SUSHI_DARGON_EYE="Dragon Eye";

    private static HashMap<String, Sushi> SUSHI_DATA = new HashMap<>();

    @Autowired
    private SushiServiceImpl sushiService;

    @PostConstruct
    public void initSushiDataToMemory(){
        List<Sushi> sushiList = sushiService.queryAllSushi();
        if(sushiList!= null && sushiList.size()!=0){
            for(Sushi sushi:sushiList){
                SUSHI_DATA.put(sushi.getName(),sushi);
            }
        }
    }

    public static Sushi getSushiData(String sushiName){
        return SUSHI_DATA.get(sushiName);
    }
}
