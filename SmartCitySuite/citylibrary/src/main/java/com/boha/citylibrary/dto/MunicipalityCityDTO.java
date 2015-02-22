
package com.boha.citylibrary.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class MunicipalityCityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer municipalityCityID;
    private CityDTO city;
    private Integer municipalityID;

    public MunicipalityCityDTO() {
    }

    public Integer getMunicipalityCityID() {
        return municipalityCityID;
    }

    public void setMunicipalityCityID(Integer municipalityCityID) {
        this.municipalityCityID = municipalityCityID;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (municipalityCityID != null ? municipalityCityID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipalityCityDTO)) {
            return false;
        }
        MunicipalityCityDTO other = (MunicipalityCityDTO) object;
        if ((this.municipalityCityID == null && other.municipalityCityID != null) || (this.municipalityCityID != null && !this.municipalityCityID.equals(other.municipalityCityID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.MunicipalityCity[ municipalityCityID=" + municipalityCityID + " ]";
    }
    
}
