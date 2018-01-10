package com.magarex.easyly.Models;

import java.util.Date;

/**
 * Created by HP on 1/1/2018.
 */

public class Order {

    private long id,customerId,operatorId;
    private String category;
    private String order;
    private String numberOfItem;
    private String price;
    private String date;
    private String time;
    private String typeOfDelivery;
    private String status;

    public Order(long id,long customerId, long operatorId, String category, String order, String numberOfItem, String price, String date, String time, String typeOfDelivery, String status) {
        this.id = id;
        this.customerId = customerId;
        this.operatorId = operatorId;
        this.category = category;
        this.order = order;
        this.numberOfItem = numberOfItem;
        this.price = price;
        this.date = date;
        this.time = time;
        this.typeOfDelivery = typeOfDelivery;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Order(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getNumberOfItem() {
        return numberOfItem;
    }

    public void setNumberOfItem(String numberOfItem) {
        this.numberOfItem = numberOfItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
