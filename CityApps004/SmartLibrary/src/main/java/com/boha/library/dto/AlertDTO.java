/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class AlertDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer alertID, municipalityID, municipalityStaffID, profileInfoID;
    private Date updated;
    private Integer id;
    private int index;
    private String description,municipalityName, profileName;
    private String title;
    private Double latitude, distance;
    private Double longitude;
    private String thumbnailURL;
    private String href;
    private Boolean activeFlag;
    private AlertTypeDTO alertType;
    private MunicipalityStaffDTO staff;
    private List<AlertImageDTO> alertImageList;

    public AlertDTO() {
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getMunicipalityStaffID() {
        return municipalityStaffID;
    }

    public void setMunicipalityStaffID(Integer municipalityStaffID) {
        this.municipalityStaffID = municipalityStaffID;
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public MunicipalityStaffDTO getStaff() {
        return staff;
    }

    public void setStaff(MunicipalityStaffDTO staff) {
        this.staff = staff;
    }

    public List<AlertImageDTO> getAlertImageList() {
        return alertImageList;
    }

    public void setAlertImageList(List<AlertImageDTO> alertImageList) {
        this.alertImageList = alertImageList;
    }

    
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public AlertTypeDTO getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertTypeDTO alertType) {
        this.alertType = alertType;
    }

    

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
        hash += (alertID != null ? alertID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlertDTO)) {
            return false;
        }
        AlertDTO other = (AlertDTO) object;
        if ((this.alertID == null && other.alertID != null) || (this.alertID != null && !this.alertID.equals(other.alertID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.Alert[ alertID=" + alertID + " ]";
    }

}
