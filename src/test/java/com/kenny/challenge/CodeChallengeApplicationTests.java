package com.kenny.challenge;

import com.kenny.challenge.entity.Sushi;
import com.kenny.challenge.repo.SushiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CodeChallengeApplicationTests {

    @Qualifier("sushiRepository")
    SushiRepository sushiRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testDAO(){
        List<Sushi> sushiList = sushiRepository.findAll();
    }

}
