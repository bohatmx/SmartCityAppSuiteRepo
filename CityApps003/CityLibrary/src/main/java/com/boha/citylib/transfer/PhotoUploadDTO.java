/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.citylib.transfer;


import com.boha.citylib.dto.AlertImageDTO;
import com.boha.citylib.dto.ComplaintImageDTO;
import com.boha.citylib.dto.MunicipalityImageDTO;
import com.boha.citylib.dto.NewsArticleImageDTO;
import com.boha.citylib.dto.StaffImageDTO;

/**
 *
 * @author aubreyM
 */
public class PhotoUploadDTO {
    private MunicipalityImageDTO municipalityImage;
    private AlertImageDTO alertImage;
    private ComplaintImageDTO complaintImage;
    private StaffImageDTO staffImage;
    private NewsArticleImageDTO newsArticleImage;

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
