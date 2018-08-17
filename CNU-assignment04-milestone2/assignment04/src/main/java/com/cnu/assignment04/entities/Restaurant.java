package com.cnu.assignment04.entities;

import com.cnu.assignment04.repositories.CuisineRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @ColumnDefault("0")
    private Float rating;

    @NotNull
    @Size(max = 255)
    private String city;

    @ManyToMany(cascade = {
                    CascadeType.ALL,
                    CascadeType.ALL
            })
    @JoinTable(name = "restaurant_cuisines",
            joinColumns = { @JoinColumn(name = "restaurant_id") },
            inverseJoinColumns = { @JoinColumn(name = "cuisine_id") })
    private Set<Cuisine> cuisines = new HashSet<>();

    @NotNull
    @Column(name="latitude", columnDefinition="Decimal(10,8)")
    private Float latitude;

    @NotNull
    @Column(name="longitude", columnDefinition="Decimal(11,8)")
    private Float longitude;

    @NotNull
    @ColumnDefault("1")
    private Boolean is_open;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "restaurant")
    private Set<Item> items = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCuisines(List<String> cuisineNames) {
        for (String cuisineName : cuisineNames) {
            addCuisine(new Cuisine(cuisineName));
        }
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setIs_open(Boolean is_open) {
        this.is_open = is_open;
    }

    public void addCuisine(Cuisine cuisine) {
        cuisines.add(cuisine);
        cuisine.getRestaurants().add(this);
    }

    @JsonIgnore
    public Set<Cuisine> getCuisineObjects() {
        return cuisines;
    }

    public Set<String> getCuisines() {
        Set<String> cuisineNames = new HashSet<>();
        for (Cuisine cuisine: cuisines) {
            cuisineNames.add(cuisine.getName());
        }

        return cuisineNames;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
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

    public String getCity() {
        return city;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getRating() {
        return rating;
    }

    public Boolean getIs_open() {
        return is_open;
    }
}
