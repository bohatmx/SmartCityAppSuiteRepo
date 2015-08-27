/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.transfer;

import com.boha.library.dto.AlertImageDTO;
import com.boha.library.dto.ComplaintImageDTO;
import com.boha.library.dto.MunicipalityImageDTO;
import com.boha.library.dto.NewsArticleImageDTO;
import com.boha.library.dto.ProfileImageDTO;
import com.boha.library.dto.StaffImageDTO;

/**
 *
 * @author aubreyM
 */
public class PhotoUploadDTO {
    public interface PhotoUploadedListener {
        public void onPhotoUploaded();
        public void onPhotoUploadFailed();
    }
    private MunicipalityImageDTO municipalityImage;
    private AlertImageDTO alertImage;
    private ComplaintImageDTO complaintImage;
    private ProfileImageDTO profileImage;
    private StaffImageDTO staffImage;
    private NewsArticleImageDTO newsArticleImage;
    private Long dateUploaded;
    private Integer municipalityID;

    public ProfileImageDTO getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImageDTO profileImage) {
        this.profileImage = profileImage;
    }

    public Long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Long dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public NewsArticleImageDTO getNewsArticleImage() {
        return newsArticleImage;
    }

    public void setNewsArticleImage(NewsArticleImageDTO newsArticleImage) {
        this.newsArticleImage = newsArticleImage;
    }
    
    

    public MunicipalityImageDTO getMunicipalityImage() {
        return municipalityImage;
    }

    public void setMunicipalityImage(MunicipalityImageDTO municipalityImage) {
        this.municipalityImage = municipalityImage;
    }

    public AlertImageDTO getAlertImage() {
        return alertImage;
    }

    public void setAlertImage(AlertImageDTO alertImage) {
        this.alertImage = alertImage;
    }

    public ComplaintImageDTO getComplaintImage() {
        return complaintImage;
    }

    public void setComplaintImage(ComplaintImageDTO complaintImage) {
        this.complaintImage = complaintImage;
    }

    public StaffImageDTO getStaffImage() {
        return staffImage;
    }

    public void setStaffImage(StaffImageDTO staffImage) {
        this.staffImage = staffImage;
    }
    
    
}
