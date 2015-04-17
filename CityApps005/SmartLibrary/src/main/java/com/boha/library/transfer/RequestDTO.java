/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.library.transfer;

import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.CityDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintFollowerDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.CountryDTO;
import com.boha.library.dto.CustomerStatusDTO;
import com.boha.library.dto.CustomerTypeDTO;
import com.boha.library.dto.FaqDTO;
import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.NewsArticleTypeDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.ProvinceDTO;
import com.boha.library.dto.StaffTypeDTO;

import java.util.List;

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
    private Integer year, month;
    private String userName, password, referenceNumber,
            gcmRegistrationID, email,municipalityName, accountNumber;
    private Double latitude = 0.0, longitude = 0.0;
    private Boolean rideWebSocket = Boolean.TRUE;
    private Boolean zipResponse = Boolean.TRUE;
    private AlertDTO alert;
    private ComplaintDTO complaint;
    private CountryDTO country;
    private ComplaintFollowerDTO complaintFollower;
    private List<ProvinceDTO> provinceList;

    private MunicipalityDTO municipality;
    private MunicipalityStaffDTO municipalityStaff;
    private ProfileInfoDTO profileInfo;
    private GcmDeviceDTO gcmDevice;
    private NewsArticleDTO newsArticle;

    private List<CityDTO> cityList;
    private List<CustomerTypeDTO> customerTypeList;
    private List<AlertTypeDTO> alertTypeList;
    private List<ComplaintTypeDTO> complaintTypelist;
    private List<NewsArticleTypeDTO> newsArticleTypeList;
    private List<StaffTypeDTO> staffTypeList;
    private List<CustomerStatusDTO> customerStatusList;
    private List<FaqDTO> faqList;

    /**
     * the following static integers represent the operations that the
     * SmartCity Cloud Application exposes for its API.
     * 
     * Each instance of RequestDTO must have a requestType equal to
     * one of static integers below.
     */
    //register
    public static final int
            REGISTER_MUNICIPALITY = 1,
            REGISTER_MUNICIPALITY_STAFF = 2,
            REGISTER_CITIZEN = 3,
            SEND_GCM_REGISTRATION = 4;

    //sign-in
    public static final int
            SIGN_IN_MUNICIPALITY_STAFF = 6,
            SIGN_IN_CITIZEN = 7;

    //Create new data
    public static final int 
            ADD_CITIES = 100,
            ADD_CUSTOMER_TYPES = 101,
            ADD_ALERT_TYPES = 102,
            ADD_COMPLAINT_TYPES = 103,
            ADD_NEWS_ARTICLE_TYPES = 104,
            ADD_STAFF_TYPE_LIST = 105,
            ADD_COUNTRY = 106,
            ADD_PROVINCES = 107,
            ADD_MUNICIPALITY_CITIES = 108,
            ADD_ALERT = 109,
            ADD_NEWS_ARTICLE = 110,
            ADD_COMPLAINT = 111,
            ADD_ACCOUNT = 112,
            ADD_CUSTOMER_STATUS_TYPES = 113,
            ADD_COMPLAINT_STATUS_UPDATE = 114,
            ADD_FREQUENTLY_ASKED_QUESTION = 115,
            ADD_GCM_DEVICE = 116,
            ADD_COMPLAINT_FOLLOWER = 117;

    //Update data
    public static final int 
            UPDATE_CITY = 1100,
            UPDATE_CUSTOMER_TYPE = 1101,
            UPDATE_ALERT_TYPE = 1102,
            UPDATE_COMPLAINT_TYPE = 1103,
            UPDATE_NEWS_ARTICLE_TYPE = 1104,
            UPDATE_STAFF_TYPE = 1105,
            UPDATE_COUNTRY = 1106,
            UPDATE_PROVINCE = 1107,
            REMOVE_MUNICIPALITY_CITY = 1108,
            UPDATE_ALERT = 1109,
            UPDATE_NEWS_ARTICLE = 1110,
            UPDATE_COMPLAINT = 1111,
            UPDATE_ACCOUNT = 1112,
            UPDATE_CUSTOMER_STATUS_TYPES = 1113,
            UPDATE_FREQUENTLY_ASKED_QUESTION = 1115;

    //get lookup lists
    public static final int 
            GET_COUNTRY_LIST = 200,
            GET_PROVINCE_LIST = 201,
            GET_CITY_LIST = 202,
            GET_MUNICIPALITY_LIST_BY_COUNTRY = 203,
            GET_ALERT_TYPE_LIST = 204,
            GET_COMPLAINT_TYPE_LIST = 205,
            GET_NEWS_ARTICLE_TYPE_LIST = 206,
            GET_STAFF_TYPE_LIST = 207,
            GET_CUSTOMER_STATUS_LIST = 208,
            GET_CITIES_BY_MUNICIPALITY = 209,
            GET_MUNICIPALITY_STAFF_LIST = 210,
            GET_MUNICIPALITY_BY_NAME = 211,
            GET_COMPLAINT_FOLLOWERS = 212,
            GET_COMPLAINT_STATUS = 213,
            GET_PDF_STATEMENT = 214;

    //get data
    public static final int 
            GET_MUNICIPALITY_DATA = 300,
            GET_ALERTS_WITHIN_RADIUS = 301,
            GET_ALERTS_BY_MUNICIPALITY = 302,
            GET_ALERTS_BY_CITY = 303,
            GET_NEWS_ARTICLES_BY_MUNICIPALITY = 304,
            GET_NEWS_ARTICLES_BY_CITY = 305,
            GET_COMPLAINTS_BY_CITIZEN = 306,
            GET_COMPLAINTS_BY_MUNICIPALITY = 307,
            GET_COMPLAINTS_BY_CITY = 308,
            GET_FREQUENTLY_ASKED_QUESTIONS = 309,
            GET_ALERTS_AROUND_ADDRESS = 310,
            GET_COMPLAINTS_WITHIN_RADIUS = 311,
            GET_COMPLAINTS_AROUND_ADDRESS = 312,
            REFRESH_CITIZEN_COMPLAINTS_FROM_SERVICE = 313,
            REFRESH_CITIZEN_ACCOUNTS = 314;

    public ComplaintFollowerDTO getComplaintFollower() {
        return complaintFollower;
    }

    public void setComplaintFollower(ComplaintFollowerDTO complaintFollower) {
        this.complaintFollower = complaintFollower;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean isRideWebSocket() {
        return rideWebSocket;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRideWebSocket() {
        return rideWebSocket;
    }

    public void setRideWebSocket(Boolean rideWebSocket) {
        this.rideWebSocket = rideWebSocket;
    }

    public Boolean getZipResponse() {
        return zipResponse;
    }

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<FaqDTO> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<FaqDTO> faqList) {
        this.faqList = faqList;
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

    public Boolean isZipResponse() {
        return zipResponse;
    }

    public void setZipResponse(Boolean zipResponse) {
        this.zipResponse = zipResponse;
    }

    public AlertDTO getAlert() {
        return alert;
    }

    public void setAlert(AlertDTO alert) {
        this.alert = alert;
    }

    public ComplaintDTO getComplaint() {
        return complaint;
    }

    public void setComplaint(ComplaintDTO complaint) {
        this.complaint = complaint;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public List<ProvinceDTO> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceDTO> provinceList) {
        this.provinceList = provinceList;
    }

    public String getGcmRegistrationID() {
        return gcmRegistrationID;
    }

    public void setGcmRegistrationID(String gcmRegistrationID) {
        this.gcmRegistrationID = gcmRegistrationID;
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

    public NewsArticleDTO getNewsArticle() {
        return newsArticle;
    }

    public void setNewsArticle(NewsArticleDTO newsArticle) {
        this.newsArticle = newsArticle;
    }

    public List<CityDTO> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDTO> cityList) {
        this.cityList = cityList;
    }

    public List<CustomerTypeDTO> getCustomerTypeList() {
        return customerTypeList;
    }

    public void setCustomerTypeList(List<CustomerTypeDTO> customerTypeList) {
        this.customerTypeList = customerTypeList;
    }

    public List<AlertTypeDTO> getAlertTypeList() {
        return alertTypeList;
    }

    public void setAlertTypeList(List<AlertTypeDTO> alertTypeList) {
        this.alertTypeList = alertTypeList;
    }

    public List<ComplaintTypeDTO> getComplaintTypelist() {
        return complaintTypelist;
    }

    public void setComplaintTypelist(List<ComplaintTypeDTO> complaintTypelist) {
        this.complaintTypelist = complaintTypelist;
    }

    public List<NewsArticleTypeDTO> getNewsArticleTypeList() {
        return newsArticleTypeList;
    }

    public void setNewsArticleTypeList(List<NewsArticleTypeDTO> newsArticleTypeList) {
        this.newsArticleTypeList = newsArticleTypeList;
    }

    public List<StaffTypeDTO> getStaffTypeList() {
        return staffTypeList;
    }

    public void setStaffTypeList(List<StaffTypeDTO> staffTypeList) {
        this.staffTypeList = staffTypeList;
    }

    public List<CustomerStatusDTO> getCustomerStatusList() {
        return customerStatusList;
    }

    public void setCustomerStatusList(List<CustomerStatusDTO> customerStatusList) {
        this.customerStatusList = customerStatusList;
    }

 

}
