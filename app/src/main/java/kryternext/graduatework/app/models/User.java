package kryternext.graduatework.app.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String shopName;
    private String type;
    private double balance;

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getShopName() {
        return shopName;
    }

    public String getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", username, email);
    }
}
