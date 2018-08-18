package com.assign04.repositories;

import com.assign04.entities.Cuisine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CuisineRepository extends CrudRepository<Cuisine, Long> {
    List<Cuisine> findByName(String name);
}