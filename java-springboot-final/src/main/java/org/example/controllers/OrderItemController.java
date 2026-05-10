package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/order-items")
@PreAuthorize("isAuthenticated()")
public class OrderItemController {
    @Autowired
    private OrderItemDao orderItemDao;

    @GetMapping
    public List<OrderItem> getAll() {
        return orderItemDao.getAll();
    }

    @GetMapping("/{id}")
    public OrderItem getById(@PathVariable int id) {
        OrderItem orderItem = orderItemDao.getById(id);
        if (orderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        return orderItem;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderItem create(@RequestBody OrderItem orderItem) {
        return orderItemDao.create(orderItem);
    }

    @PutMapping("/{id}")
    public OrderItem update(@PathVariable int id, @RequestBody OrderItem orderItem) {
        if (orderItemDao.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        orderItem.setId(id);
        return orderItemDao.update(orderItem);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderItemDao.delete(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        return rowsAffected;
    }
}
