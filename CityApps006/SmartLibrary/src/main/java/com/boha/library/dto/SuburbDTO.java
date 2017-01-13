/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author aubreymalabie
 */
public class SuburbDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer suburbID;
    private String suburbName, districtName;
    private Double latitude;
    private Double longitude;
    private Integer municipalityID;
    private List<SuburbMessageDTO> suburbMessageList;
    private Integer systemID;
    private Integer districtID;

    public SuburbDTO() {
    }

    public SuburbDTO(Integer suburbID, String suburbName) {
        this.suburbID = suburbID;
        this.suburbName = suburbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Integer districtID) {
        this.districtID = districtID;
    }

    public Integer getSystemID() {
        return systemID;
    }

    public void setSystemID(Integer systemID) {
        this.systemID = systemID;
    }

    public Integer getSuburbID() {
        return suburbID;
    }

    public void setSuburbID(Integer suburbID) {
        this.suburbID = suburbID;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public void setSuburbName(String suburbName) {
        this.suburbName = suburbName;
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

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }


    public List<SuburbMessageDTO> getSuburbMessageList() {
        return suburbMessageList;
    }

    public void setSuburbMessageList(List<SuburbMessageDTO> suburbMessageList) {
        this.suburbMessageList = suburbMessageList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (suburbID != null ? suburbID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuburbDTO)) {
            return false;
        }
        SuburbDTO other = (SuburbDTO) object;
        if ((this.suburbID == null && other.suburbID != null) || (this.suburbID != null && !this.suburbID.equals(other.suburbID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.Suburb[ suburbID=" + suburbID + " ]";
    }

}
