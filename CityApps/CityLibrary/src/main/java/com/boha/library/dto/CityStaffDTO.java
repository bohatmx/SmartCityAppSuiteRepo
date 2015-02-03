/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class CityStaffDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer cityStaffID;
    private String firstName;
    private String lastName;
    private String email;
    private String cellphone;
    private Date dateRegistered;
    private String password;
    private Boolean activeFlag;
    private Integer cityID;

    public CityStaffDTO() {
    }

    public CityStaffDTO(Integer cityStaffID) {
        this.cityStaffID = cityStaffID;
    }


    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }
    

    public Integer getCityStaffID() {
        return cityStaffID;
    }

    public void setCityStaffID(Integer cityStaffID) {
        this.cityStaffID = cityStaffID;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cityStaffID != null ? cityStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CityStaffDTO)) {
            return false;
        }
        CityStaffDTO other = (CityStaffDTO) object;
        if ((this.cityStaffID == null && other.cityStaffID != null) || (this.cityStaffID != null && !this.cityStaffID.equals(other.cityStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.CityStaff[ cityStaffID=" + cityStaffID + " ]";
    }
    
}
