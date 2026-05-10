package org.example.daos;

import org.example.exceptions.DaoException;
import org.example.models.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Order> getAll() {
        return jdbcTemplate.query("SELECT * FROM orders ORDER BY id", this::mapToOrder);
    }

    public Order getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?", this::mapToOrder, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Order create(Order order) {
        String sql = "INSERT INTO orders (username) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, order.getUsername());
            return statement;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new DaoException("Failed to create order.");
        }
        return getById(key.intValue());
    }

    public Order update(Order order) {
        int rowsAffected = jdbcTemplate.update("UPDATE orders SET username = ? WHERE id = ?", order.getUsername(), order.getId());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        }
        return getById(order.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM orders WHERE id = ?", id);
    }

    private Order mapToOrder(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Order(
                resultSet.getInt("id"),
                resultSet.getString("username")
        );
    }
}
