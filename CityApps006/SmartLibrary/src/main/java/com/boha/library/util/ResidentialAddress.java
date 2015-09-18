package com.boha.library.util;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/09/11.
 */
public class ResidentialAddress implements Serializable{
    private String number, street, suburb, city;

    public ResidentialAddress(String number, String street, String suburb, String city) {
        this.number = number.trim();
        this.street = street.trim();
        this.suburb = suburb.trim();
        this.city = city.trim();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
