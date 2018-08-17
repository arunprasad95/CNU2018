package com.cnu.assignment04.controllers;

import com.cnu.assignment04.entities.Cuisine;
import com.cnu.assignment04.entities.Restaurant;
import com.cnu.assignment04.repositories.CuisineRepository;
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
@RequestMapping(path="/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    private Boolean validateRestaurant(Restaurant restaurant) {
        if (!(
                restaurant.getName() != null &&
                restaurant.getCity() != null &&
                restaurant.getLatitude() != null &&
                restaurant.getLongitude() != null &&
                restaurant.getRating() != null &&
                restaurant.getIs_open() != null &&
                restaurant.getRating() >= 0 &&
                restaurant.getLongitude() >= -180 &&
                restaurant.getLongitude() <= 180 &&
                restaurant.getLatitude() >= -90 &&
                restaurant.getLatitude() <= 90
        )) return false;

        for (Cuisine cuisine : restaurant.getCuisineObjects()) {
            if (cuisine.getName() == null) {
                return false;
            }
        }

        return true;
    }

    @GetMapping(path="/{restaurantId}")
    public @ResponseBody ResponseEntity<HTTPResponse> getRestaurant(
            @PathVariable("restaurantId") Integer restaurantId
    ) throws Exception {

        Restaurant restaurant;
        try {
            restaurant = restaurantRepository.findById(restaurantId);
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
            if (validateRestaurant(restaurant)) restaurantRepository.save(restaurant);
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
            restaurant = restaurantRepository.findById(restaurantId);
            if (restaurant == null) throw new Exception("Restaurant Not Found");
            restaurantRepository.delete(restaurant);
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HTTPResponse>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path="/{restaurantId}")
    public @ResponseBody ResponseEntity putRestaurant(@PathVariable("restaurantId") Integer restaurantId, @RequestBody Restaurant restaurant) throws Exception {
        try {
            Restaurant oldRestaurant = restaurantRepository.findById(restaurantId);
            if (oldRestaurant == null) return new ResponseEntity<HTTPResponse>(new FailureResponse("Restaurant not found"), HttpStatus.BAD_REQUEST);
            if (validateRestaurant(restaurant)) {
                restaurant.setId(restaurantId);
                restaurantRepository.save(restaurant);
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
            restaurants = restaurantRepository.findAllByNameContainingAndCityContaining(name, city);
        }
        else {
            restaurants = restaurantRepository.findAllByNameContainingAndCityContainingAndCuisineContaining(name, city, cuisine);
        }

        return new ResponseEntity<HTTPResponse>(new SuccessResponse(restaurants), HttpStatus.OK);
    }

}