

package com.boha.cityapps.data.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class NewsArticleTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer newsArticleTypeID;
    private String newsArticleTypeName;
    private Integer color;
    private List<NewsArticleDTO> newsArticleList;
    private Integer municipalityID;

    public NewsArticleTypeDTO() {
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

   

    public Integer getNewsArticleTypeID() {
        return newsArticleTypeID;
    }

    public void setNewsArticleTypeID(Integer newsArticleTypeID) {
        this.newsArticleTypeID = newsArticleTypeID;
    }

    public String getNewsArticleTypeName() {
        return newsArticleTypeName;
    }

    public void setNewsArticleTypeName(String newsArticleTypeName) {
        this.newsArticleTypeName = newsArticleTypeName;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public List<NewsArticleDTO> getNewsArticleList() {
        return newsArticleList;
    }

    public void setNewsArticleList(List<NewsArticleDTO> newsArticleList) {
        this.newsArticleList = newsArticleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (newsArticleTypeID != null ? newsArticleTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsArticleTypeDTO)) {
            return false;
        }
        NewsArticleTypeDTO other = (NewsArticleTypeDTO) object;
        if ((this.newsArticleTypeID == null && other.newsArticleTypeID != null) || (this.newsArticleTypeID != null && !this.newsArticleTypeID.equals(other.newsArticleTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.NewsArticleType[ newsArticleTypeID=" + newsArticleTypeID + " ]";
    }
    
}
