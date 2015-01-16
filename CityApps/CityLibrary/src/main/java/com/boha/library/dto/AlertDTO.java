package com.boha.library.dto;

import java.util.Date;

/**
 * Created by aubreyM on 15/01/13.
 */
public class AlertDTO {
    Integer alertID, alertCategoryID, citizenID, alertColor;
    String message, categoryName;
    Double latitude, longitude;
    Date alertDate;

    public Integer getAlertColor() {
        return alertColor;
    }

    public void setAlertColor(Integer alertColor) {
        this.alertColor = alertColor;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(Integer citizenID) {
        this.citizenID = citizenID;
    }

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public Integer getAlertCategoryID() {
        return alertCategoryID;
    }

    public void setAlertCategoryID(Integer alertCategoryID) {
        this.alertCategoryID = alertCategoryID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }
}
