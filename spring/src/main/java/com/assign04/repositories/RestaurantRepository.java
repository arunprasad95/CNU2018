package com.assign04.repositories;

import com.assign04.entities.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RestaurantRepository extends CrudRepository<Restaurant,Long> {
    Restaurant findById(Integer id);
    Set<Restaurant> findAllByNameContainingAndCityContaining(String name, String city);
    @Query("SELECT r FROM Restaurant r JOIN r.cuisines c WHERE c.name LIKE %?3% " +
            "AND r.name LIKE %?1% AND r.city LIKE %?2%")
    Set<Restaurant> findAllByNameContainingAndCityContainingAndCuisineContaining(String name , String city , String cuisineName);
}
