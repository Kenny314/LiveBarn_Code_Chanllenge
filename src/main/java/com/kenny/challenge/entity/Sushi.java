package com.kenny.challenge.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * POJO Shshi
 */
@Entity
@Table(name = "sushi")
public class Sushi {
    @Id
    private Long id;

    @Column(name="name",length = 30)
    private String name;

    @Column(name="time_to_make")
    private Integer timeToMake;

    @OneToMany(mappedBy = "sushi",cascade = CascadeType.ALL)
    private Set<SushiOrder> sushiOrders = new HashSet<>();

    protected Sushi() {
    }

    public Sushi(String name, Integer timeToMake) {
        this.name = name;
        this.timeToMake = timeToMake;
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

    public Integer getTimeToMake() {
        return timeToMake;
    }

    public void setTimeToMake(Integer timeToMake) {
        this.timeToMake = timeToMake;
    }

    public Set<SushiOrder> getSushiOrders() {
        return sushiOrders;
    }

    public void setSushiOrders(Set<SushiOrder> sushiOrders) {
        this.sushiOrders = sushiOrders;
    }

    @Override
    public String toString() {
        return "Sushi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", timeToMake=" + timeToMake +
                '}';
    }
}
