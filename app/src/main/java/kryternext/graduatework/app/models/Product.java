package kryternext.graduatework.app.models;


public class Product {
    private String name;
    private String description;
    private String type;
    private int count;
    private double price;

    public String getProductName() {
        return name;
    }

    public void setProductName(String name) {
        this.name = name;
    }

    public String getProductDescription() {
        return description;
    }

    public void setProductDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return type;
    }

    public void setProductType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
