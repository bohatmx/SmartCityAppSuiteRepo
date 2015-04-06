
package com.boha.citylib.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class StaffTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer staffTypeID;
    private String staffTypeName;
    private List<MunicipalityStaffDTO> municipalityStaffList;

    public StaffTypeDTO() {
    }


    public Integer getStaffTypeID() {
        return staffTypeID;
    }

    public void setStaffTypeID(Integer staffTypeID) {
        this.staffTypeID = staffTypeID;
    }

    public String getStaffTypeName() {
        return staffTypeName;
    }

    public void setStaffTypeName(String staffTypeName) {
        this.staffTypeName = staffTypeName;
    }

    public List<MunicipalityStaffDTO> getMunicipalityStaffList() {
        return municipalityStaffList;
    }

    public void setMunicipalityStaffList(List<MunicipalityStaffDTO> municipalityStaffList) {
        this.municipalityStaffList = municipalityStaffList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffTypeID != null ? staffTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffTypeDTO)) {
            return false;
        }
        StaffTypeDTO other = (StaffTypeDTO) object;
        if ((this.staffTypeID == null && other.staffTypeID != null) || (this.staffTypeID != null && !this.staffTypeID.equals(other.staffTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.StaffType[ staffTypeID=" + staffTypeID + " ]";
    }
    
}
