package com.medicine.emedic.model;

public class Order {
    private String order_id;
    private String order_status;
    private String order_date;

    public Order(String order_id, String order_status, String order_date)
    {
        this.order_id = order_id;
        this.order_status = order_status;
        this.order_date = order_date;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public String getOrder_status()
    {
        return order_status;
    }

    public void setOrder_status(String order_status)
    {
        this.order_status = order_status;
    }

    public String getOrder_date()
    {
        return order_date;
    }

    public void setOrder_date(String order_date)
    {
        this.order_date = order_date;
    }
}
