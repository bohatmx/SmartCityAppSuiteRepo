

package com.boha.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class ComplaintImageDTO implements Serializable, ImageInterface {
    private static final long serialVersionUID = 1L;
    private Integer complaintImageID, complaintID, municipalityID;
    private String fileName, localFilepath;
    private Long dateUploaded;
    private Long dateTaken;
    private Double latitude;
    private Double longitude;
    private Boolean activeFlag;

    public String getLocalFilepath() {
        return localFilepath;
    }

    public void setLocalFilepath(String localFilepath) {
        this.localFilepath = localFilepath;
    }

    public ComplaintImageDTO() {
    }

    public ComplaintImageDTO(Integer complaintImageID) {
        this.complaintImageID = complaintImageID;
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

    public Integer getComplaintImageID() {
        return complaintImageID;
    }

    public Integer getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(Integer complaintID) {
        this.complaintID = complaintID;
    }

    public void setComplaintImageID(Integer complaintImageID) {
        this.complaintImageID = complaintImageID;
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
        hash += (complaintImageID != null ? complaintImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintImageDTO)) {
            return false;
        }
        ComplaintImageDTO other = (ComplaintImageDTO) object;
        if ((this.complaintImageID == null && other.complaintImageID != null) || (this.complaintImageID != null && !this.complaintImageID.equals(other.complaintImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.ComplaintImage[ complaintImageID=" + complaintImageID + " ]";
    }
    
}
