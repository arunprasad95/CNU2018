package com.cnu.assignment04.controllers;

import com.cnu.assignment04.entities.Cuisine;
import com.cnu.assignment04.entities.Item;
import com.cnu.assignment04.entities.Restaurant;
import com.cnu.assignment04.repositories.CuisineRepository;
import com.cnu.assignment04.repositories.ItemRepository;
import com.cnu.assignment04.repositories.RestaurantRepository;
import com.cnu.assignment04.response.FailureResponse;
import com.cnu.assignment04.response.HTTPResponse;
import com.cnu.assignment04.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping(path="/api/items")
public class ItemsController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(path="")
    public @ResponseBody
    ResponseEntity<HTTPResponse> getItems(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            @RequestParam(value = "minPrice", required = false) Float minPrice) {

        Set<Item> items;
        if (name == null) name = "";
        if (maxPrice == null) maxPrice = Float.MAX_VALUE;
        if (minPrice == null) minPrice = 0f;

        items = itemRepository.findAllByNameContainingAndPriceBetween(name, minPrice, maxPrice);

        return new ResponseEntity<HTTPResponse>(new SuccessResponse(items), HttpStatus.OK);
    }

}