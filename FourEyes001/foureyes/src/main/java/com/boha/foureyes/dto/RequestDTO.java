/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.foureyes.dto;

/**
 * This class represents all requests made to the cloud web application. All requests to either
 * the servlet or the web socket is in the form of a JSON string that is converted to a RequestDTO 
 * object instance when received. The requestType is one of the types listed below.
 * 
 * Only properties or fields required by the request are populated during any request. e.g. in order to
 * sign a user in, a username and password are required as well as the appropriate requestType.
 * 
 * Requests with zipResponse set to true will have the response compressed before transfer to requestor.
 * 
 * @author aubreyM
 */
public class RequestDTO {
    public RequestDTO(Integer requestType) {
        this.requestType = requestType;
    }

    private Integer requestType, radius = 20,
            municipalityID;
    private Long startDate, endDate;
    private Integer year, month;
    private String userName, password, referenceNumber,
            gcmRegistrationID, email,municipalityName, accountNumber;
    private Double latitude = 0.0, longitude = 0.0;
    private Boolean rideWebSocket = Boolean.TRUE;
    private Boolean zipResponse = Boolean.TRUE;


    private MunicipalityDTO municipality;
    private MunicipalityStaffDTO municipalityStaff;
    private ProfileInfoDTO profileInfo;
    private GcmDeviceDTO gcmDevice;

    public static final int 

            GET_ERROR_REPORTS = 315;

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getGcmRegistrationID() {
        return gcmRegistrationID;
    }

    public void setGcmRegistrationID(String gcmRegistrationID) {
        this.gcmRegistrationID = gcmRegistrationID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Boolean isRideWebSocket() {
        return rideWebSocket;
    }

    public void setRideWebSocket(Boolean rideWebSocket) {
        this.rideWebSocket = rideWebSocket;
    }

    public Boolean isZipResponse() {
        return zipResponse;
    }

    public void setZipResponse(Boolean zipResponse) {
        this.zipResponse = zipResponse;
    }

    public MunicipalityDTO getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityDTO municipality) {
        this.municipality = municipality;
    }

    public MunicipalityStaffDTO getMunicipalityStaff() {
        return municipalityStaff;
    }

    public void setMunicipalityStaff(MunicipalityStaffDTO municipalityStaff) {
        this.municipalityStaff = municipalityStaff;
    }

    public ProfileInfoDTO getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfoDTO profileInfo) {
        this.profileInfo = profileInfo;
    }

    public GcmDeviceDTO getGcmDevice() {
        return gcmDevice;
    }

    public void setGcmDevice(GcmDeviceDTO gcmDevice) {
        this.gcmDevice = gcmDevice;
    }
}


