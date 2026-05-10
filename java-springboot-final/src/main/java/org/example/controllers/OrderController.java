package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable int id) {
        Order order = orderDao.getById(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderDao.create(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable int id, @RequestBody Order order) {
        if (orderDao.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        order.setId(id);
        return orderDao.update(order);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderDao.delete(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return rowsAffected;
    }
}
