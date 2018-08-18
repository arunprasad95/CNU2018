package com.assign04.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;
    @NotNull
    private Float price;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Float getPrice() {
        return price;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
}
