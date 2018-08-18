package com.assign04.controllers;


import com.assign04.entities.Restaurant;
import com.assign04.repositories.CuisineRepository;
import com.assign04.repositories.RestaurantRepository;
import com.assign04.response.FailureResponse;
import com.assign04.response.HTTPResponse;
import com.assign04.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path="/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantRepository RR;
    @Autowired
    private CuisineRepository CR;

    @GetMapping(path="/{restaurantId}")
    public @ResponseBody
    ResponseEntity<HTTPResponse> getRestaurant(
            @PathVariable("restaurantId") Integer restaurantId
    ) throws Exception {

        Restaurant restaurant;
        try {
            restaurant = RR.findById(restaurantId);
            if (restaurant == null) {
                return new ResponseEntity<HTTPResponse>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HTTPResponse>(new SuccessResponse(restaurant), HttpStatus.OK);
    }

    @PostMapping(path = "")
    public @ResponseBody ResponseEntity<HTTPResponse> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            if (restaurant.validate()) RR.save(restaurant);
            else throw new Exception("INVALID INPUT");
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<HTTPResponse>(new SuccessResponse(restaurant), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{restaurantId}")
    public @ResponseBody ResponseEntity<HTTPResponse> deleteRestaurant(@PathVariable("restaurantId") Integer restaurantId) throws Exception {
        Restaurant restaurant;
        try {
            restaurant = RR.findById(restaurantId);
            if (restaurant == null) throw new Exception("Restaurant Not Found");
            RR.delete(restaurant);
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HTTPResponse>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path="/{restaurantId}")
    public @ResponseBody ResponseEntity putRestaurant(@PathVariable("restaurantId") Integer restaurantId, @RequestBody Restaurant restaurant) throws Exception {
        try {
            Restaurant oldRestaurant = RR.findById(restaurantId);
            if (oldRestaurant == null) return new ResponseEntity<HTTPResponse>(new FailureResponse("Restaurant not found"), HttpStatus.BAD_REQUEST);
            if (restaurant.validate()) {
                restaurant.setId(restaurantId);
                RR.save(restaurant);
            }
            else {
                throw new Exception("Invalid input");
            }
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<HTTPResponse>(new HTTPResponse("success"), HttpStatus.OK);
    }

    @GetMapping(path="")
    public @ResponseBody
    ResponseEntity<HTTPResponse> getRestaurants(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "city", required = false) String city) {

        Set<Restaurant> restaurants;
        if (name == null) name = "";
        if (city == null) city = "";
        if (cuisine == null) {
            restaurants = RR.findAllByNameContainingAndCityContaining(name, city);
        }
        else {
            restaurants = RR.findAllByNameContainingAndCityContainingAndCuisineContaining(name, city, cuisine);
        }

        return new ResponseEntity<HTTPResponse>(new SuccessResponse(restaurants), HttpStatus.OK);
    }

}