
package com.boha.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class StaffImageDTO implements Serializable,ImageInterface {
    private static final long serialVersionUID = 1L;
    private Integer staffImageID;
    private String fileName, localFilepath;
    private Long dateUploaded;
    private Long dateTaken;
    private Double latitude;
    private Double longitude;
    private Integer municipalityStaffID, municipalityID;
    private Boolean activeFlag;

    public StaffImageDTO() {
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

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getStaffImageID() {
        return staffImageID;
    }

    public void setStaffImageID(Integer staffImageID) {
        this.staffImageID = staffImageID;
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

    public Integer getMunicipalityStaffID() {
        return municipalityStaffID;
    }

    public void setMunicipalityStaffID(Integer municipalityStaffID) {
        this.municipalityStaffID = municipalityStaffID;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffImageID != null ? staffImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffImageDTO)) {
            return false;
        }
        StaffImageDTO other = (StaffImageDTO) object;
        if ((this.staffImageID == null && other.staffImageID != null) || (this.staffImageID != null && !this.staffImageID.equals(other.staffImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.StaffImage[ staffImageID=" + staffImageID + " ]";
    }
    
}
