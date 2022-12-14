package com.kenny.challenge.repo;

import com.kenny.challenge.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Status DAO
 */
@Repository
public interface StatusRepository extends JpaRepository<Status,Long> {
}
