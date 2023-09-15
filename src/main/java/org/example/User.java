package org.example;

public class User {
    private String name;
    private String date;
    private String phone;

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Date: " + date + "\n" +
                "Phone: " + phone;
    }
}
