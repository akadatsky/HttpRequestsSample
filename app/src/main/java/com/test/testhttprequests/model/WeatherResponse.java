package com.test.testhttprequests.model;

public class WeatherResponse {

    private WeatherMain main;

    private int id;

    private String name;

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "main=" + main +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
