package kryternext.graduatework.app.models;

import java.util.Date;

public class Order {
    private long timestamp;
    private Date date;
    private User user;
    private double cost;
    private boolean isConfirmed;
    public Order() {
    }

}
