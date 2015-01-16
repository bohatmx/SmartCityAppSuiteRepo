package com.boha.library.dto;

/**
 * Created by aubreyM on 15/01/13.
 */
public class AlertCategoryDTO {
    Integer alertCategoryID;
    String categoryName;

    public AlertCategoryDTO(Integer alertCategoryID, String categoryName) {
        this.alertCategoryID = alertCategoryID;
        this.categoryName = categoryName;
    }

    public Integer getAlertCategoryID() {
        return alertCategoryID;
    }

    public void setAlertCategoryID(Integer alertCategoryID) {
        this.alertCategoryID = alertCategoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
