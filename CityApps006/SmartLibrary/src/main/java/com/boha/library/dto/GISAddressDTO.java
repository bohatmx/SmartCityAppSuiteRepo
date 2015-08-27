package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/07/30.
 */
public class GISAddressDTO implements Serializable{

    private String street, suburb,city;

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
