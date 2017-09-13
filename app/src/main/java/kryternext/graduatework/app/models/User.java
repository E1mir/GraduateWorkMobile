package kryternext.graduatework.app.models;

public class User {
    private String username;
    private String email;
    private String shopName;
    private String type;
    private double balance;

    private boolean isAuthorized;

    public User() {
        isAuthorized = true;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
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

}
