/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto.transfer;

import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.AlertDTO;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.PhotoUploadDTO;
import com.boha.library.dto.ProfileInfoDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ResponseDTO implements Serializable{
    private int statusCode;
    private String message, sessionID;
    private double requestResponseTimeSeconds;
    private List<AlertDTO> alertList;
    private List<AccountDTO> accountList;
    private ProfileInfoDTO profileInfoDTO;
    private List<ComplaintTypeDTO> complaintTypeList = new ArrayList<>();
    private List<ComplaintDTO> complaintList = new ArrayList<>();
    private List<AlertTypeDTO> alertTypeList = new ArrayList<>();
    private List<PhotoUploadDTO> photoUploadList = new ArrayList<>();

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
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

    
    public double getRequestResponseTimeSeconds() {
        return requestResponseTimeSeconds;
    }

    public void setRequestResponseTimeSeconds(double requestResponseTimeSeconds) {
        this.requestResponseTimeSeconds = requestResponseTimeSeconds;
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

    public ProfileInfoDTO getProfileInfoDTO() {
        return profileInfoDTO;
    }

    public void setProfileInfoDTO(ProfileInfoDTO profileInfoDTO) {
        this.profileInfoDTO = profileInfoDTO;
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
