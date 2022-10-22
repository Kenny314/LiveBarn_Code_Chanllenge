package com.kenny.challenge.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sushi")
public class Sushi {
    @Id
    private Long id;

    @Column(name="name",length = 30)
    private String name;

    @Column(name="time_to_make")
    private Timestamp timeToMake;

    @OneToMany(mappedBy = "sushi",cascade = CascadeType.ALL)
    private Set<SushiOrder> sushiOrders = new HashSet<>();

    protected Sushi() {
    }

    public Sushi(String name, Timestamp timeToMake) {
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

    public Timestamp getTimeToMake() {
        return timeToMake;
    }

    public void setTimeToMake(Timestamp timeToMake) {
        this.timeToMake = timeToMake;
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
