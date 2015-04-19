package com.boha.foureyes.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class MunicipalityDTO implements Serializable {

    private String serviceURI;
    private List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    private List<GcmDeviceDTO> gcmDeviceList;
    private static final long serialVersionUID = 1L;
    private Integer municipalityID, countryID;
    private String municipalityName, countryName;
    private String email;
    private String cellphone;
    private Double latitude;
    private Double longitude;
    private Date dateRegistered;
    private Boolean activeFlag;

    private List<ProfileInfoDTO> profileInfoList;
    private List<MunicipalityStaffDTO> municipalityStaffList;

    public String getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public List<ErrorStoreAndroidDTO> getErrorStoreAndroidList() {
        return errorStoreAndroidList;
    }

    public void setErrorStoreAndroidList(List<ErrorStoreAndroidDTO> errorStoreAndroidList) {
        this.errorStoreAndroidList = errorStoreAndroidList;
    }

    public List<GcmDeviceDTO> getGcmDeviceList() {
        return gcmDeviceList;
    }

    public void setGcmDeviceList(List<GcmDeviceDTO> gcmDeviceList) {
        this.gcmDeviceList = gcmDeviceList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public List<ProfileInfoDTO> getProfileInfoList() {
        return profileInfoList;
    }

    public void setProfileInfoList(List<ProfileInfoDTO> profileInfoList) {
        this.profileInfoList = profileInfoList;
    }

    public List<MunicipalityStaffDTO> getMunicipalityStaffList() {
        return municipalityStaffList;
    }

    public void setMunicipalityStaffList(List<MunicipalityStaffDTO> municipalityStaffList) {
        this.municipalityStaffList = municipalityStaffList;
    }
}
