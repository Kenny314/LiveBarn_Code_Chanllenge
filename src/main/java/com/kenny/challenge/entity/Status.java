package com.kenny.challenge.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * POJO Status
 */
@Entity
@Table(name = "status")
public class Status {

    @Id
    private Long id;

    @Column(name="name")
    private String name;
    @OneToMany(mappedBy = "status",cascade = CascadeType.ALL)
    private Set<SushiOrder> sushiOrders;

    protected Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SushiOrder> getSushiOrders() {
        return sushiOrders;
    }

    public void setSushiOrders(Set<SushiOrder> sushiOrders) {
        this.sushiOrders = sushiOrders;
    }
}
