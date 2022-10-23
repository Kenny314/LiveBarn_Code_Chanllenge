package com.kenny.challenge.entity.view;

import java.sql.Timestamp;

public class Order {
    private Long id;
    private Long statusId;
    private Long sushiId;

    private Long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getSushiId() {
        return sushiId;
    }

    public void setSushiId(Long sushiId) {
        this.sushiId = sushiId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
