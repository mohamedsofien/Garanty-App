package com.example.fossien.Garanty.data;

/**
 * Created by fossien on 25/07/17.
 */

public class User {

    private String email;
    private String name;
    private String type;
    private String adr;
    private String lat;
    private String lon;
    private String logo;
    private String validationItem;

    public User(String email, String name, String type, String adr, String lat, String lon, String logo, String validationItem) {

        this.email = email;
        this.name = name;
        this.type = type;
        this.adr = adr;
        this.lat = lat;
        this.lon = lon;
        this.logo = logo;
        this.validationItem = validationItem;

    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getValidationItem() {
        return validationItem;
    }

    public void setValidationItem(String validationItem) {
        this.validationItem = validationItem;
    }

}
