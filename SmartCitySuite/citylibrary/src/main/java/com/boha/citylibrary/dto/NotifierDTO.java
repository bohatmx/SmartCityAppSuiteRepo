package com.boha.citylibrary.dto;

/**
 * Created by aubreyM on 15/02/21.
 */
public class NotifierDTO {
    private Integer color;
    private String message;
    private AlertDTO alert;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertDTO getAlert() {
        return alert;
    }

    public void setAlert(AlertDTO alert) {
        this.alert = alert;
    }
}
