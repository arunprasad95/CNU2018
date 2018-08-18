package com.assign04.repositories;

import com.assign04.entities.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends CrudRepository<Item, Long> {
    public Item findById(Integer id);
    public Set<Item> findAllByNameContainingAndPriceBetween(String name, Float minPrice, Float maxPrice);

}
