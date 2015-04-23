
package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class MunicipalityStaffDTO implements Serializable {
    private List<AlertDTO> alertList;
    private List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    private List<ComplaintUpdateStatusDTO> complaintUpdateStatusList;
    private List<GcmDeviceDTO> gcmDeviceList;
    private static final long serialVersionUID = 1L;
    private Integer municipalityStaffID;
    private String firstName;
    private String lastName;
    private String email;
    private String cellphone;
    private Date dateRegistered;
    private String password;
    private Boolean activeFlag;
    private List<StaffImageDTO> staffImageList;
    private Integer municipalityID;
    private StaffTypeDTO staffType;

    public MunicipalityStaffDTO() {
    }


    
    public Integer getMunicipalityStaffID() {
        return municipalityStaffID;
    }

    public void setMunicipalityStaffID(Integer municipalityStaffID) {
        this.municipalityStaffID = municipalityStaffID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public List<StaffImageDTO> getStaffImageList() {
        return staffImageList;
    }

    public void setStaffImageList(List<StaffImageDTO> staffImageList) {
        this.staffImageList = staffImageList;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public StaffTypeDTO getStaffType() {
        return staffType;
    }

    public void setStaffType(StaffTypeDTO staffType) {
        this.staffType = staffType;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (municipalityStaffID != null ? municipalityStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipalityStaffDTO)) {
            return false;
        }
        MunicipalityStaffDTO other = (MunicipalityStaffDTO) object;
        if ((this.municipalityStaffID == null && other.municipalityStaffID != null) || (this.municipalityStaffID != null && !this.municipalityStaffID.equals(other.municipalityStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.MunicipalityStaff[ municipalityStaffID=" + municipalityStaffID + " ]";
    }

    public List<ErrorStoreAndroidDTO> getErrorStoreAndroidList() {
        return errorStoreAndroidList;
    }

    public void setErrorStoreAndroidList(List<ErrorStoreAndroidDTO> errorStoreAndroidList) {
        this.errorStoreAndroidList = errorStoreAndroidList;
    }

    public List<ComplaintUpdateStatusDTO> getComplaintUpdateStatusList() {
        return complaintUpdateStatusList;
    }

    public void setComplaintUpdateStatusList(List<ComplaintUpdateStatusDTO> complaintUpdateStatusList) {
        this.complaintUpdateStatusList = complaintUpdateStatusList;
    }

    public List<GcmDeviceDTO> getGcmDeviceList() {
        return gcmDeviceList;
    }

    public void setGcmDeviceList(List<GcmDeviceDTO> gcmDeviceList) {
        this.gcmDeviceList = gcmDeviceList;
    }

    public List<AlertDTO> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<AlertDTO> alertList) {
        this.alertList = alertList;
    }
}
