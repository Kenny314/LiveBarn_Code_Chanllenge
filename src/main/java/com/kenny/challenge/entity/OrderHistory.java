package com.kenny.challenge.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * keep order status change information
 * 1.created - inprocess , save inprocess time
 * 2.inprocess - pause , save paused time
 * 3.inprocess - finishi , save finishi time
 */
@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = SushiOrder.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName="id")
    private SushiOrder order;

    @ManyToOne(targetEntity = Status.class,fetch = FetchType.EAGER)
    @JoinColumn(name="status_id",referencedColumnName="id")
    private Status status;

    @Column(name = "created_At")
    private Timestamp createAt;

    public OrderHistory() {
    }

    public OrderHistory(Long id, SushiOrder order, Status status, Timestamp createAt) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SushiOrder getOrder() {
        return order;
    }

    public void setOrder(SushiOrder order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
