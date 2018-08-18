package com.assign04.repositories;

import com.assign04.entities.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RestaurantRepository extends CrudRepository<Restaurant,Long> {
    Restaurant findById(Integer id);
}
