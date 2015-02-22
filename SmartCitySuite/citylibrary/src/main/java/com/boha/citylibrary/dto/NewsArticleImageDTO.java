
package com.boha.citylibrary.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class NewsArticleImageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer newsArticleImageID;
    private String fileName;

    public String getLocalFilepath() {
        return localFilepath;
    }

    public void setLocalFilepath(String localFilepath) {
        this.localFilepath = localFilepath;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    private String localFilepath;
    private Date dateTaken;
    private Date dateUploaded;
    private Double latitude;
    private Double longitude;
    private Integer newsArticleID, municipalityID;
    private Boolean activeFlag;

    public NewsArticleImageDTO() {
    }



    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getNewsArticleImageID() {
        return newsArticleImageID;
    }

    public void setNewsArticleImageID(Integer newsArticleImageID) {
        this.newsArticleImageID = newsArticleImageID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
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

    public Integer getNewsArticleID() {
        return newsArticleID;
    }

    public void setNewsArticleID(Integer newsArticleID) {
        this.newsArticleID = newsArticleID;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (newsArticleImageID != null ? newsArticleImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsArticleImageDTO)) {
            return false;
        }
        NewsArticleImageDTO other = (NewsArticleImageDTO) object;
        if ((this.newsArticleImageID == null && other.newsArticleImageID != null) || (this.newsArticleImageID != null && !this.newsArticleImageID.equals(other.newsArticleImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.NewsArticleImage[ newsArticleImageID=" + newsArticleImageID + " ]";
    }

}
