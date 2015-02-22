
package com.boha.citylibrary.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class CustomerTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer customerTypeID;
    private String customerTypeName;
    private List<ProfileInfoDTO> profileInfoList;

    public CustomerTypeDTO() {
    }

    public Integer getCustomerTypeID() {
        return customerTypeID;
    }

    public void setCustomerTypeID(Integer customerTypeID) {
        this.customerTypeID = customerTypeID;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public List<ProfileInfoDTO> getProfileInfoList() {
        return profileInfoList;
    }

    public void setProfileInfoList(List<ProfileInfoDTO> profileInfoList) {
        this.profileInfoList = profileInfoList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerTypeID != null ? customerTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerTypeDTO)) {
            return false;
        }
        CustomerTypeDTO other = (CustomerTypeDTO) object;
        if ((this.customerTypeID == null && other.customerTypeID != null) || (this.customerTypeID != null && !this.customerTypeID.equals(other.customerTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.CustomerType[ customerTypeID=" + customerTypeID + " ]";
    }
    
}
