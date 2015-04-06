
package com.boha.cityapps.data.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class CustomerEmailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer customerEmailID;
    private String email;
    private Date dateRegistered;
    private Boolean activeFlag;
    private Integer profileInfoID;

    public CustomerEmailDTO() {
    }

    public Integer getCustomerEmailID() {
        return customerEmailID;
    }

    public void setCustomerEmailID(Integer customerEmailID) {
        this.customerEmailID = customerEmailID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
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
        hash += (customerEmailID != null ? customerEmailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerEmailDTO)) {
            return false;
        }
        CustomerEmailDTO other = (CustomerEmailDTO) object;
        if ((this.customerEmailID == null && other.customerEmailID != null) || (this.customerEmailID != null && !this.customerEmailID.equals(other.customerEmailID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.CustomerEmail[ customerEmailID=" + customerEmailID + " ]";
    }
    
}
