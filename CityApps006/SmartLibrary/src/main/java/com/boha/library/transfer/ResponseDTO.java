package com.boha.library.transfer;

import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.CityDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.ComplaintUpdateStatusDTO;
import com.boha.library.dto.CountryDTO;
import com.boha.library.dto.CustomerStatusDTO;
import com.boha.library.dto.CustomerTypeDTO;
import com.boha.library.dto.ErrorStoreAndroidDTO;
import com.boha.library.dto.ErrorStoreDTO;
import com.boha.library.dto.FNBandNedbankResponseDTO;
import com.boha.library.dto.FaqDTO;
import com.boha.library.dto.FreqQuestionTypeDTO;
import com.boha.library.dto.GISAddressDTO;
import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.NewsArticleDTO;
import com.boha.library.dto.NewsArticleTypeDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.ProvinceDTO;
import com.boha.library.dto.StaffTypeDTO;
import com.boha.library.dto.UserDTO;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents the response from the server. A status code of 0
 * indicates successful completion of the request. An non-zero status code
 * indicates an error that is described in the message property.
 *
 * SmartCity Cloud Platform application always returns an object of this class.
 *
 * If incoming request had zipResponse set to true, this object will be
 * compressed by GZip.
 *
 * @author aubreyM
 */
public class ResponseDTO implements Serializable {

    //response status
    private int statusCode = 0;
    private String message, sessionID, gcmRegistrationID, log, data;
    private Double requestResponseTimeSeconds;
    private Boolean municipalityAccessFailed = Boolean.FALSE;
    private List<String> pdfFileNameList;
    private Integer goodResponses = 0, badResponses = 0;

    //response lists
    private FNBandNedbankResponseDTO FNBandNedbankResponse;
    private List<GISAddressDTO> addressList;
    private List<PhotoUploadDTO> photoUploadList;
    private List<AlertDTO> alertList;
    private List<AccountDTO> accountList;
    private List<ProfileInfoDTO> profileInfoList;
    private List<ComplaintTypeDTO> complaintTypeList;
    private List<ComplaintCategoryDTO> complaintCategoryList;
    private List<ComplaintDTO> complaintList;
    private List<AlertTypeDTO> alertTypeList;
    private List<CityDTO> cityList;
    private List<ProvinceDTO> provinceList;
    private List<ComplaintUpdateStatusDTO> complaintUpdateStatusList;
    private List<CountryDTO> countryList;
    private List<CustomerStatusDTO> customerStatusList;
    private List<CustomerTypeDTO> customerTypeList;
    private List<ErrorStoreDTO> errorStoreList;
    private List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    private List<GcmDeviceDTO> gcmDeviceList;
    private List<MunicipalityDTO> municipalityList;
    private List<MunicipalityStaffDTO> municipalityStaffList;
    private List<NewsArticleTypeDTO> newsArticleTypeList;
    private List<NewsArticleDTO> newsArticleList;
    private List<StaffTypeDTO> staffTypeList;
    private List<FaqDTO> faqList;
    private List<FreqQuestionTypeDTO> faqTypeList;
    private List<UserDTO> userList;

    public FNBandNedbankResponseDTO getFNBandNedbankResponse() {
        return FNBandNedbankResponse;
    }

    public void setFNBandNedbankResponse(FNBandNedbankResponseDTO FNBandNedbankResponse) {
        this.FNBandNedbankResponse = FNBandNedbankResponse;
    }

    public List<ComplaintCategoryDTO> getComplaintCategoryList() {
        return complaintCategoryList;
    }

    public void setComplaintCategoryList(List<ComplaintCategoryDTO> complaintCategoryList) {
        this.complaintCategoryList = complaintCategoryList;
    }

    public List<GISAddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<GISAddressDTO> addressList) {
        this.addressList = addressList;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getMunicipalityAccessFailed() {
        return municipalityAccessFailed;
    }

    public Integer getGoodResponses() {
        return goodResponses;
    }

    public void setGoodResponses(Integer goodResponses) {
        this.goodResponses = goodResponses;
    }

    public Integer getBadResponses() {
        return badResponses;
    }

    public void setBadResponses(Integer badResponses) {
        this.badResponses = badResponses;
    }

    public Boolean isMunicipalityAccessFailed() {
        return municipalityAccessFailed;
    }

    public void setMunicipalityAccessFailed(Boolean municipalityAccessFailed) {
        this.municipalityAccessFailed = municipalityAccessFailed;
    }

    public List<UserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }

    public List<String> getPdfFileNameList() {
        return pdfFileNameList;
    }

    public void setPdfFileNameList(List<String> pdfFileNameList) {
        this.pdfFileNameList = pdfFileNameList;
    }

    public List<FreqQuestionTypeDTO> getFaqTypeList() {
        return faqTypeList;
    }

    public void setFaqTypeList(List<FreqQuestionTypeDTO> faqTypeList) {
        this.faqTypeList = faqTypeList;
    }

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getGcmRegistrationID() {
        return gcmRegistrationID;
    }

    public void setGcmRegistrationID(String gcmRegistrationID) {
        this.gcmRegistrationID = gcmRegistrationID;
    }

    
    public Double getRequestResponseTimeSeconds() {
        return requestResponseTimeSeconds;
    }

    public void setRequestResponseTimeSeconds(Double requestResponseTimeSeconds) {
        this.requestResponseTimeSeconds = requestResponseTimeSeconds;
    }

    public List<ProfileInfoDTO> getProfileInfoList() {
        return profileInfoList;
    }

    public void setProfileInfoList(List<ProfileInfoDTO> profileInfoList) {
        this.profileInfoList = profileInfoList;
    }

    public List<FaqDTO> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<FaqDTO> faqList) {
        this.faqList = faqList;
    }

    public List<CityDTO> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDTO> cityList) {
        this.cityList = cityList;
    }

    public List<ProvinceDTO> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceDTO> provinceList) {
        this.provinceList = provinceList;
    }

    public List<ComplaintUpdateStatusDTO> getComplaintUpdateStatusList() {
        return complaintUpdateStatusList;
    }

    public void setComplaintUpdateStatusList(List<ComplaintUpdateStatusDTO> complaintUpdateStatusList) {
        this.complaintUpdateStatusList = complaintUpdateStatusList;
    }

    public List<CountryDTO> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryDTO> countryList) {
        this.countryList = countryList;
    }

    public List<CustomerStatusDTO> getCustomerStatusList() {
        return customerStatusList;
    }

    public void setCustomerStatusList(List<CustomerStatusDTO> customerStatusList) {
        this.customerStatusList = customerStatusList;
    }

    public List<CustomerTypeDTO> getCustomerTypeList() {
        return customerTypeList;
    }

    public void setCustomerTypeList(List<CustomerTypeDTO> customerTypeList) {
        this.customerTypeList = customerTypeList;
    }

    public List<ErrorStoreDTO> getErrorStoreList() {
        return errorStoreList;
    }

    public void setErrorStoreList(List<ErrorStoreDTO> errorStoreList) {
        this.errorStoreList = errorStoreList;
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

    public List<MunicipalityDTO> getMunicipalityList() {
        return municipalityList;
    }

    public void setMunicipalityList(List<MunicipalityDTO> municipalityList) {
        this.municipalityList = municipalityList;
    }

    public List<MunicipalityStaffDTO> getMunicipalityStaffList() {
        return municipalityStaffList;
    }

    public void setMunicipalityStaffList(List<MunicipalityStaffDTO> municipalityStaffList) {
        this.municipalityStaffList = municipalityStaffList;
    }

    public List<NewsArticleTypeDTO> getNewsArticleTypeList() {
        return newsArticleTypeList;
    }

    public void setNewsArticleTypeList(List<NewsArticleTypeDTO> newsArticleTypeList) {
        this.newsArticleTypeList = newsArticleTypeList;
    }

    public List<NewsArticleDTO> getNewsArticleList() {
        return newsArticleList;
    }

    public void setNewsArticleList(List<NewsArticleDTO> newsArticleList) {
        this.newsArticleList = newsArticleList;
    }

    public List<StaffTypeDTO> getStaffTypeList() {
        return staffTypeList;
    }

    public void setStaffTypeList(List<StaffTypeDTO> staffTypeList) {
        this.staffTypeList = staffTypeList;
    }

    public List<AlertTypeDTO> getAlertTypeList() {
        return alertTypeList;
    }

    public void setAlertTypeList(List<AlertTypeDTO> alertTypeList) {
        this.alertTypeList = alertTypeList;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AlertDTO> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<AlertDTO> alertList) {
        this.alertList = alertList;
    }

    public List<AccountDTO> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDTO> accountList) {
        this.accountList = accountList;
    }

    public List<ComplaintTypeDTO> getComplaintTypeList() {
        return complaintTypeList;
    }

    public void setComplaintTypeList(List<ComplaintTypeDTO> complaintTypeList) {
        this.complaintTypeList = complaintTypeList;
    }

    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintDTO> complaintList) {
        this.complaintList = complaintList;
    }

}
