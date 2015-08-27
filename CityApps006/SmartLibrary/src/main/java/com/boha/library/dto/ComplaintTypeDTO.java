
package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ComplaintTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer complaintTypeID, municipalityID, color, id;
    private String complaintTypeName,categoryName;
    private Boolean locationIsRequired = Boolean.TRUE;
    private Boolean pictureRequired = Boolean.TRUE;
    private List<ComplaintDTO> complaintList;
    private Integer complaintCategoryID;

    public static final int RED = 1, AMBER = 2, GREEN = 3;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getLocationIsRequired() {
        return locationIsRequired;
    }

    public Boolean getPictureRequired() {
        return pictureRequired;
    }

    public Integer getComplaintCategoryID() {
        return complaintCategoryID;
    }

    public void setComplaintCategoryID(Integer complaintCategoryID) {
        this.complaintCategoryID = complaintCategoryID;
    }

    public Boolean isLocationIsRequired() {
        return locationIsRequired;
    }

    public Boolean isPictureRequired() {
        return pictureRequired;
    }

    public void setPictureRequired(Boolean pictureRequired) {
        this.pictureRequired = pictureRequired;
    }

    public void setLocationIsRequired(Boolean locationIsRequired) {
        this.locationIsRequired = locationIsRequired;
    }

    public ComplaintTypeDTO() {
    }


    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    
    public Integer getComplaintTypeID() {
        return complaintTypeID;
    }

    public void setComplaintTypeID(Integer complaintTypeID) {
        this.complaintTypeID = complaintTypeID;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintDTO> complaintList) {
        this.complaintList = complaintList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (complaintTypeID != null ? complaintTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintTypeDTO)) {
            return false;
        }
        ComplaintTypeDTO other = (ComplaintTypeDTO) object;
        if ((this.complaintTypeID == null && other.complaintTypeID != null) || (this.complaintTypeID != null && !this.complaintTypeID.equals(other.complaintTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.ComplaintType[ complaintTypeID=" + complaintTypeID + " ]";
    }
    
}
