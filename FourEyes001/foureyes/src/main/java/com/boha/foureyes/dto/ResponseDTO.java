package com.boha.foureyes.dto;



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

    private int statusCode = 0;
    private String message, sessionID, gcmRegistrationID, log;
    private Double requestResponseTimeSeconds;

    //response lists

    private List<ErrorStoreDTO> errorStoreList;
    private List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    private List<GcmDeviceDTO> gcmDeviceList;
    private List<MunicipalityDTO> municipalityList;
    private List<MunicipalityStaffDTO> municipalityStaffList;
    private List<SummaryDTO> summaryList;

    public List<SummaryDTO> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<SummaryDTO> summaryList) {
        this.summaryList = summaryList;
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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getGcmRegistrationID() {
        return gcmRegistrationID;
    }

    public void setGcmRegistrationID(String gcmRegistrationID) {
        this.gcmRegistrationID = gcmRegistrationID;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Double getRequestResponseTimeSeconds() {
        return requestResponseTimeSeconds;
    }

    public void setRequestResponseTimeSeconds(Double requestResponseTimeSeconds) {
        this.requestResponseTimeSeconds = requestResponseTimeSeconds;
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
}
