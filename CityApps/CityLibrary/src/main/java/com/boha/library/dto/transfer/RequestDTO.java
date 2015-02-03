/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto.transfer;

import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.ComplaintDTO;

/**
 *
 * @author aubreyM
 */
public class RequestDTO {

    public RequestDTO(int requestType) {
        this.requestType = requestType;
    }

    private int requestType, radius = 50;
    private String userName, password;
    private double latitude, longitude;
    private AlertDTO alert;
    private ComplaintDTO complaint;
    private boolean rideWebSocket = true;

    public static final int LOGIN = 1,
            ADD_ALERT = 2,
            GET_ALERTS = 3;
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isRideWebSocket() {
        return rideWebSocket;
    }

    public void setRideWebSocket(boolean rideWebSocket) {
        this.rideWebSocket = rideWebSocket;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getUserName() {
        return userName;
    }

    public AlertDTO getAlert() {
        return alert;
    }

    public void setAlert(AlertDTO alert) {
        this.alert = alert;
    }

    public ComplaintDTO getComplaint() {
        return complaint;
    }

    public void setComplaint(ComplaintDTO complaint) {
        this.complaint = complaint;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    
}
