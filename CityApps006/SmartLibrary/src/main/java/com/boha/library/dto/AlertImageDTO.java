/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class AlertImageDTO implements Serializable, ImageInterface {
    private static final long serialVersionUID = 1L;
    private Integer alertImageID;
    private String fileName, localFilepath;
    private Long dateUploaded;
    private Long dateTaken;
    private Double latitude;
    private Double longitude;
    private Integer alertID, municipalityID;
    private Boolean activeFlag;
    private String url;
    private String secureUrl;

    private Integer height;
    private Integer width;
    private Integer bytes;

    public String getUrl() {
        return url;
    }
    @Override
    public File getFile() {
        if (localFilepath != null) {
            return new File(localFilepath);
        }
        return null;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    public AlertImageDTO() {
    }

    public AlertImageDTO(Integer alertImageID) {
        this.alertImageID = alertImageID;
    }

    public String getLocalFilepath() {
        return localFilepath;
    }

    public void setLocalFilepath(String localFilepath) {
        this.localFilepath = localFilepath;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getAlertImageID() {
        return alertImageID;
    }

    public void setAlertImageID(Integer alertImageID) {
        this.alertImageID = alertImageID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Long dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Long dateTaken) {
        this.dateTaken = dateTaken;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertImageID != null ? alertImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlertImageDTO)) {
            return false;
        }
        AlertImageDTO other = (AlertImageDTO) object;
        if ((this.alertImageID == null && other.alertImageID != null) || (this.alertImageID != null && !this.alertImageID.equals(other.alertImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.AlertImage[ alertImageID=" + alertImageID + " ]";
    }
    
}
