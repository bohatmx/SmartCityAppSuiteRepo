
package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ComplaintDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer complaintID, municipalityID;
    private Integer id, numberOfFollowers;
    private String referenceNumber;
    private String remarks, address;
    private Long complaintDate;
    private Double latitude, distance;
    private Double longitude;
    private Boolean activeFlag;
    private ProfileInfoDTO profileInfo;
    private ComplaintTypeDTO complaintType;
    private int index;

    private List<ComplaintImageDTO> complaintImageList;
    private List<ComplaintUpdateStatusDTO> complaintUpdateStatusList;

    public Integer getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(Integer numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public ComplaintDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ComplaintUpdateStatusDTO> getComplaintUpdateStatusList() {
        return complaintUpdateStatusList;
    }

    public void setComplaintUpdateStatusList(List<ComplaintUpdateStatusDTO> complaintUpdateStatusList) {
        this.complaintUpdateStatusList = complaintUpdateStatusList;
    }

    public List<ComplaintImageDTO> getComplaintImageList() {
        return complaintImageList;
    }

    public void setComplaintImageList(List<ComplaintImageDTO> complaintImageList) {
        this.complaintImageList = complaintImageList;
    }

    
    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public ProfileInfoDTO getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;
    }

    public ComplaintTypeDTO getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(ComplaintTypeDTO complaintType) {
        this.complaintType = complaintType;
    }

    public Integer getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(Integer complaintID) {
        this.complaintID = complaintID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(Long complaintDate) {
        this.complaintDate = complaintDate;
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

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (complaintID != null ? complaintID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintDTO)) {
            return false;
        }
        ComplaintDTO other = (ComplaintDTO) object;
        if ((this.complaintID == null && other.complaintID != null) || (this.complaintID != null && !this.complaintID.equals(other.complaintID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.Complaint[ complaintID=" + complaintID + " ]";
    }
    
}
