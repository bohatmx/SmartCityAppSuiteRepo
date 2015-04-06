
package com.boha.cityapps.data.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class CityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer cityID;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private Integer provinceID;
    private List<ComplaintDTO> complaintList;

    public CityDTO() {
    }

   

    public Integer getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(Integer provinceID) {
        this.provinceID = provinceID;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintDTO> complaintList) {
        this.complaintList = complaintList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cityID != null ? cityID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CityDTO)) {
            return false;
        }
        CityDTO other = (CityDTO) object;
        if ((this.cityID == null && other.cityID != null) || (this.cityID != null && !this.cityID.equals(other.cityID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.City[ cityID=" + cityID + " ]";
    }
    
}
