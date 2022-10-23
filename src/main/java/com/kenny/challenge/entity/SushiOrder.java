package com.kenny.challenge.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sushi_order")
public class SushiOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    @ManyToOne(targetEntity = Status.class,fetch = FetchType.EAGER)
    @JoinColumn(name="status_id",referencedColumnName="id")
    private Status status;

    @ManyToOne(targetEntity = Sushi.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "sushi_id",referencedColumnName="id")
    private Sushi sushi;

    @Column(name = "created_At")
    private Timestamp createdAt;


    public SushiOrder(Status status, Sushi sushi) {
        this.status = status;
        this.sushi = sushi;
    }

    public SushiOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Sushi getSushi() {
        return sushi;
    }

    public void setSushi(Sushi sushi) {
        this.sushi = sushi;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
