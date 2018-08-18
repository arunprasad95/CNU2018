package com.assign04.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Float rating;
    private String city;
    private Float latitude;
    private Float longitude;
    private Boolean is_open;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "restaurant")
    private Set<Item> items = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.ALL,
            CascadeType.ALL
    })
    @JoinTable(name = "restaurant_cuisines",
            joinColumns = { @JoinColumn(name = "restaurant_id") },
            inverseJoinColumns = { @JoinColumn(name = "cuisine_id") })
    private Set<Cuisine> cuisines = new HashSet<>();

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public void setCity(String city) {
        this.city = city;
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
    public void setItems(Set<Item> items) {
        this.items = items;
    }
    public void setCuisines(List<String> cuisineNames) {
        for (String cuisineName : cuisineNames) {
            addCuisine(new Cuisine(cuisineName));
        }
    }
    public void addCuisine(Cuisine cuisine) {
        cuisines.add(cuisine);
        cuisine.getRestaurants().add(this);
    }
    @JsonIgnore
    public Set<Cuisine> getCuisineObjects() {
        return cuisines;
    }
    public Set<Item> getItems() {
        return items;
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

    public boolean validate()
    {
        if((id==null)||(name==null)||(city==null)||(is_open==null))
            return false;
        for(Cuisine cuisine : cuisines)
            if( cuisine.getName()== null)
                return false;

            return true;

    }
}
