package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nkululeko on 2017/01/16.
 */

public class DistrictDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer districtID;
    private String districtName;
    private Integer municipalityID;
    private List<SuburbDTO> suburbList;
    private List<DistrictMessageDTO> districtMessages;

    public DistrictDTO() {
    }

    public List<DistrictMessageDTO> getDistrictMessages() {
        return districtMessages;
    }

    public void setDistrictMessages(List<DistrictMessageDTO> districtMessages) {
        this.districtMessages = districtMessages;
    }



    public Integer getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Integer districtID) {
        this.districtID = districtID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public List<SuburbDTO> getSuburbList() {
        return suburbList;
    }

    public void setSuburbList(List<SuburbDTO> suburbList) {
        this.suburbList = suburbList;
    }
}
