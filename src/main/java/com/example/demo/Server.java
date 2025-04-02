package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {
    @Autowired JdbcTemplate jdbcTemplate;

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
    public String login(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        return jdbcTemplate.queryForObject("SELECT role FROM user WHERE username = ? AND password = ?", String.class, username, password);
    }

    @GetMapping("/manage")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    


}