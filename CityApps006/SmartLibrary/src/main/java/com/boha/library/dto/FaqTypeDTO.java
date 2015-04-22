package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreyM on 15/04/22.
 */
public class FaqTypeDTO implements Serializable{
    private Integer municipalityID;
    private static final long serialVersionUID = 1L;
    private Integer faqTypeID;
    private String faqTypeName;
    private List<FaqDTO> faqList;

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getFaqTypeID() {
        return faqTypeID;
    }

    public void setFaqTypeID(Integer faqTypeID) {
        this.faqTypeID = faqTypeID;
    }

    public String getFaqTypeName() {
        return faqTypeName;
    }

    public void setFaqTypeName(String faqTypeName) {
        this.faqTypeName = faqTypeName;
    }

    public List<FaqDTO> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<FaqDTO> faqList) {
        this.faqList = faqList;
    }
}
