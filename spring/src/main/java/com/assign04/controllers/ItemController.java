package com.assign04.controllers;

import com.assign04.entities.Item;
import com.assign04.entities.Restaurant;
import com.assign04.repositories.ItemRepository;
import com.assign04.repositories.RestaurantRepository;
import com.assign04.response.FailureResponse;
import com.assign04.response.HTTPResponse;
import com.assign04.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/restaurants/{restaurantId}/items")

public class ItemController {
    @Autowired
    private ItemRepository IR;
    @Autowired
    private RestaurantRepository RR;

    @GetMapping(path = "/{itemId}")
    public @ResponseBody
    ResponseEntity<HTTPResponse> getItem(@PathVariable("restaurantId") Integer restaurantId, @PathVariable("itemId") Integer itemId) throws Exception {
        Item item;
        try
        {
            item = IR.findById(itemId);
            if(item == null || (item.getRestaurant().getId()!=restaurantId))
                return new ResponseEntity<HTTPResponse>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HTTPResponse>(new SuccessResponse(item), HttpStatus.OK);

    }

    @PostMapping(path="", produces = "application/json", consumes = "application/json")
    public @ResponseBody ResponseEntity<HTTPResponse> createItem(@PathVariable("restaurantId") Integer restaurantId, @RequestBody Item item) throws Exception {
        Restaurant restaurant;
        try {
            restaurant = RR.findById(restaurantId);
            if (restaurant == null) throw new Exception("Restaurant not found");
            item.setRestaurant(restaurant);
            IR.save(item);
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<HTTPResponse>(new SuccessResponse(item), HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{itemId}")
    public @ResponseBody ResponseEntity<HTTPResponse> deleteItem(@PathVariable("restaurantId") Integer restaurantId, @PathVariable("itemId") Integer itemId) {
        try {
            Item item = IR.findById(itemId);
            if (item == null || !item.getRestaurant().getId().equals(restaurantId)) {
                throw new Exception("Restaurant not found");
            }
            IR.delete(item);
        } catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<HTTPResponse>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(path="/{itemId}")
    public @ResponseBody ResponseEntity putItem(@PathVariable("restaurantId") Integer restaurantId, @PathVariable("itemId") Integer itemId, @RequestBody Item item) throws Exception {
        try {
            Restaurant restaurant = RR.findById(restaurantId);
            if (restaurant == null) return new ResponseEntity<HTTPResponse>(new FailureResponse("Restaurant not found"), HttpStatus.BAD_REQUEST);
            Item oldItem = IR.findById(itemId);
            if (oldItem == null || !oldItem.getRestaurant().getId().equals(restaurantId)) return new ResponseEntity<HTTPResponse>(new FailureResponse("Item not found"), HttpStatus.BAD_REQUEST);
            if (item.validate()) {
                item.setId(itemId);
                item.setRestaurant(restaurant);
                IR.save(item);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<HTTPResponse>(new FailureResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<HTTPResponse>(new HTTPResponse("success"), HttpStatus.OK);
    }

    @GetMapping(path="")
    public @ResponseBody Iterable<Item> getItems(@PathVariable("restaurantId") Integer restaurantId) throws Exception {
        Restaurant restaurant = RR.findById(restaurantId);
        return restaurant.getItems();
    }
}
