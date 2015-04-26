
package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class NewsArticleDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer newsArticleID, id;
    private String newsText;
    private String thumbnailURL;
    private String href;
    private Double latitude;
    private Double longitude;
    private Long newsDate;
    private Boolean activeFlag;
    private Integer color;
    private CityDTO city;
    private int index;
    private Integer municipalityID;
    private NewsArticleTypeDTO newsArticleType;
    private List<NewsArticleImageDTO> newsArticleImageList;

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<NewsArticleImageDTO> getNewsArticleImageList() {
        return newsArticleImageList;
    }

    public void setNewsArticleImageList(List<NewsArticleImageDTO> newsArticleImageList) {
        this.newsArticleImageList = newsArticleImageList;
    }

    public NewsArticleDTO() {
    }


    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }


    public Integer getNewsArticleID() {
        return newsArticleID;
    }

    public void setNewsArticleID(Integer newsArticleID) {
        this.newsArticleID = newsArticleID;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
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

    public Long getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Long newsDate) {
        this.newsDate = newsDate;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public NewsArticleTypeDTO getNewsArticleType() {
        return newsArticleType;
    }

    public void setNewsArticleType(NewsArticleTypeDTO newsArticleType) {
        this.newsArticleType = newsArticleType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (newsArticleID != null ? newsArticleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsArticleDTO)) {
            return false;
        }
        NewsArticleDTO other = (NewsArticleDTO) object;
        if ((this.newsArticleID == null && other.newsArticleID != null) || (this.newsArticleID != null && !this.newsArticleID.equals(other.newsArticleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.NewsArticle[ newsArticleID=" + newsArticleID + " ]";
    }

}
