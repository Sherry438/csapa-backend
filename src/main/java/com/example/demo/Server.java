package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // Create an API
    @GetMapping("/hello")
    public String test() {
        return "hello";
    }

    @GetMapping("/testdb")
    public List<Map<String, Object>> testdb() {
        List<Map<String, Object>> userList = jdbcTemplate.queryForList("SELECT * FROM user");
        return userList;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        return jdbcTemplate.queryForMap("SELECT id, role FROM user WHERE username = ? AND password = ?",
                new Object[] { username, password });
    }

    @GetMapping("/sku")
    public List<Map<String, Object>> getSku() {
        return jdbcTemplate.queryForList("SELECT * FROM sku");
    }

    @PostMapping("/order/{user_id}")
    public boolean order(@PathVariable String user_id, @RequestBody Map<String, Object> data) {
        for (String sku_id : data.keySet()) {
            Integer stock = jdbcTemplate.queryForObject("SELECT stock FROM sku WHERE id = ?", Integer.class, sku_id);
            if (stock != null && stock < (Integer) data.get(sku_id)) {
                return false;
            }
        }
        for (String sku_id : data.keySet()) {
            jdbcTemplate.update("UPDATE sku SET stock = stock - ? WHERE id = ?", data.get(sku_id), sku_id);
            jdbcTemplate.update("INSERT INTO `order` (user_id, sku_id, num) VALUES (?, ?, ?)", user_id, sku_id,
                    data.get(sku_id));
        }
        return true;
    }

    @PostMapping("/manage")
    public boolean manage(@RequestBody List<Map<String, Object>> data) {
        for (Map<String, Object> item : data) {
            jdbcTemplate.update("UPDATE sku SET price = ?, stock = ? WHERE id = ?", item.get("price"),
                    item.get("stock"), item.get("id"));
        }
        return true;
    }

}