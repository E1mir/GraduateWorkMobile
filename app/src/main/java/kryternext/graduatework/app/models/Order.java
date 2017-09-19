package kryternext.graduatework.app.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private long orderTimestamp;
    private String username;
    private User user;
    private double cost;
    public Map<String, String> orderProductList = new HashMap<>();

    public Order() {
    }

    public long getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(long orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Map<String, String> getProducts() {
        return this.orderProductList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
