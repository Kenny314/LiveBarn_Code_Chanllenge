package com.kenny.challenge.repo;

import com.kenny.challenge.entity.Sushi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SushiRepository extends JpaRepository<Sushi,Long> {
    public Sushi findSushiByName(String name);
}
