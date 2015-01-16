package com.boha.library.dto;

import java.util.Date;

/**
 * Created by aubreyM on 15/01/13.
 */
public class CitizenDTO {
    String firstName, lastName, IDNumber;
    Integer citizenID;
    Date dateRegistered, dateLastAccess;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public Integer getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(Integer citizenID) {
        this.citizenID = citizenID;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getDateLastAccess() {
        return dateLastAccess;
    }

    public void setDateLastAccess(Date dateLastAccess) {
        this.dateLastAccess = dateLastAccess;
    }
}
