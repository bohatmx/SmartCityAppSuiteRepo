
package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class CustomerStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer customerStatusID;
    private String customerStatusName;
    private List<ProfileInfoDTO> profileInfoList;

    public CustomerStatusDTO() {
    }


    public Integer getCustomerStatusID() {
        return customerStatusID;
    }

    public void setCustomerStatusID(Integer customerStatusID) {
        this.customerStatusID = customerStatusID;
    }

    public String getCustomerStatusName() {
        return customerStatusName;
    }

    public void setCustomerStatusName(String customerStatusName) {
        this.customerStatusName = customerStatusName;
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
        hash += (customerStatusID != null ? customerStatusID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerStatusDTO)) {
            return false;
        }
        CustomerStatusDTO other = (CustomerStatusDTO) object;
        if ((this.customerStatusID == null && other.customerStatusID != null) || (this.customerStatusID != null && !this.customerStatusID.equals(other.customerStatusID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.CustomerStatus[ customerStatusID=" + customerStatusID + " ]";
    }
    
}
