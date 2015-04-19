package com.boha.foureyes.dto;

import java.io.Serializable;

/**
 * Summar for each muni
 * Created by aubreyM on 15/04/18.
 */
public class SummaryDTO implements Serializable{
    private Integer
            municipalityID,
            numberOfStaff,
            numberOFAlerts,
            numberOfCitizens,
            numberOfAlertImages,
            numberOfComplaintImages,
            numberOfComplaints;
    private String municipalityName;
    private Long startDate, endDate;
    private AlertDTO lastAlert;
    private int imageResource;
    private ComplaintDTO lastComplaint;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getNumberOfStaff() {
        return numberOfStaff;
    }

    public void setNumberOfStaff(Integer numberOfStaff) {
        this.numberOfStaff = numberOfStaff;
    }

    public Integer getNumberOFAlerts() {
        return numberOFAlerts;
    }

    public void setNumberOFAlerts(Integer numberOFAlerts) {
        this.numberOFAlerts = numberOFAlerts;
    }

    public Integer getNumberOfCitizens() {
        return numberOfCitizens;
    }

    public void setNumberOfCitizens(Integer numberOfCitizens) {
        this.numberOfCitizens = numberOfCitizens;
    }

    public Integer getNumberOfAlertImages() {
        return numberOfAlertImages;
    }

    public void setNumberOfAlertImages(Integer numberOfAlertImages) {
        this.numberOfAlertImages = numberOfAlertImages;
    }

    public Integer getNumberOfComplaintImages() {
        return numberOfComplaintImages;
    }

    public void setNumberOfComplaintImages(Integer numberOfComplaintImages) {
        this.numberOfComplaintImages = numberOfComplaintImages;
    }

    public Integer getNumberOfComplaints() {
        return numberOfComplaints;
    }

    public void setNumberOfComplaints(Integer numberOfComplaints) {
        this.numberOfComplaints = numberOfComplaints;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
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

    public AlertDTO getLastAlert() {
        return lastAlert;
    }

    public void setLastAlert(AlertDTO lastAlert) {
        this.lastAlert = lastAlert;
    }

    public ComplaintDTO getLastComplaint() {
        return lastComplaint;
    }

    public void setLastComplaint(ComplaintDTO lastComplaint) {
        this.lastComplaint = lastComplaint;
    }
}
