
package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class ProfileImageDTO implements Serializable,ImageInterface {
    private static final long serialVersionUID = 1L;
    private Integer profileImageID, municipalityID;
    private String fileName, localFilepath;
    private Date dateTaken;
    private Date dateUploaded;
    private Double latitude;
    private Double longitude;
    private Integer profileInfoID;
    private Boolean activeFlag;

    public ProfileImageDTO() {
    }


    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
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

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getProfileImageID() {
        return profileImageID;
    }

    public void setProfileImageID(Integer profileImageID) {
        this.profileImageID = profileImageID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
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

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profileImageID != null ? profileImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfileImageDTO)) {
            return false;
        }
        ProfileImageDTO other = (ProfileImageDTO) object;
        if ((this.profileImageID == null && other.profileImageID != null) || (this.profileImageID != null && !this.profileImageID.equals(other.profileImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.ProfileImage[ profileImageID=" + profileImageID + " ]";
    }
    
}
