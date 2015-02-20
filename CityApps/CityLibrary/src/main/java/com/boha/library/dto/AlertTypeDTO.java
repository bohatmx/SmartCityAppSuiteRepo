
package com.boha.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class AlertTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer alertTypeID, municipalityID;
    private String alertTypeName;
    private Integer color;

    public static final int RED = 1, AMBER = 2, GREEN = 3;

    public AlertTypeDTO() {
    }


    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public String getAlertTypeName() {
        return alertTypeName;
    }

    public void setAlertTypeName(String alertTypeName) {
        this.alertTypeName = alertTypeName;
    }

  
    public Integer getAlertTypeID() {
        return alertTypeID;
    }

    public void setAlertTypeID(Integer alertTypeID) {
        this.alertTypeID = alertTypeID;
    }

    public String getAlertTypeNmae() {
        return alertTypeName;
    }

    public void setAlertTypeNmae(String alertTypeNmae) {
        this.alertTypeName = alertTypeNmae;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertTypeID != null ? alertTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlertTypeDTO)) {
            return false;
        }
        AlertTypeDTO other = (AlertTypeDTO) object;
        if ((this.alertTypeID == null && other.alertTypeID != null) || (this.alertTypeID != null && !this.alertTypeID.equals(other.alertTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.AlertType[ alertTypeID=" + alertTypeID + " ]";
    }
    
}
