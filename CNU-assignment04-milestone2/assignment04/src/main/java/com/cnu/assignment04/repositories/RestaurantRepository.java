package com.cnu.assignment04.repositories;

import com.cnu.assignment04.entities.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    Restaurant findById(Integer id);

    Set<Restaurant> findAllByNameContainingAndCityContaining(String name, String city);

    @Query("SELECT r FROM Restaurant r JOIN r.cuisines c WHERE c.name LIKE %?3% " +
            "AND r.name LIKE %?1% AND r.city LIKE %?2%")
    Set<Restaurant> findAllByNameContainingAndCityContainingAndCuisineContaining(String name, String city, String cuisineName);

}