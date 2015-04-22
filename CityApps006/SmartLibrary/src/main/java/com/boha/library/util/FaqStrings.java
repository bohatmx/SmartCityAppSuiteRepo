package com.boha.library.util;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/04/15.
 */
public class FaqStrings implements Serializable {

    private String accountsFAQ, buildingPlansFAQ,
            cleaningWasteFAQ,
            electricityFAQ, healthFAQ,
            metroPoliceFAQ, ratesTaxesFAQ,
            socialServicesFAQ, waterSanitationFAQ;

    public String getAccountsFAQ() {
        return accountsFAQ;
    }

    public void setAccountsFAQ(String accountsFAQ) {
        this.accountsFAQ = accountsFAQ;
    }

    public String getBuildingPlansFAQ() {
        return buildingPlansFAQ;
    }

    public void setBuildingPlansFAQ(String buildingPlansFAQ) {
        this.buildingPlansFAQ = buildingPlansFAQ;
    }

    public String getCleaningWasteFAQ() {
        return cleaningWasteFAQ;
    }

    public void setCleaningWasteFAQ(String cleaningWasteFAQ) {
        this.cleaningWasteFAQ = cleaningWasteFAQ;
    }

    public String getElectricityFAQ() {
        return electricityFAQ;
    }

    public void setElectricityFAQ(String electricityFAQ) {
        this.electricityFAQ = electricityFAQ;
    }

    public String getHealthFAQ() {
        return healthFAQ;
    }

    public void setHealthFAQ(String healthFAQ) {
        this.healthFAQ = healthFAQ;
    }

    public String getMetroPoliceFAQ() {
        return metroPoliceFAQ;
    }

    public void setMetroPoliceFAQ(String metroPoliceFAQ) {
        this.metroPoliceFAQ = metroPoliceFAQ;
    }

    public String getRatesTaxesFAQ() {
        return ratesTaxesFAQ;
    }

    public void setRatesTaxesFAQ(String ratesTaxesFAQ) {
        this.ratesTaxesFAQ = ratesTaxesFAQ;
    }

    public String getSocialServicesFAQ() {
        return socialServicesFAQ;
    }

    public void setSocialServicesFAQ(String socialServicesFAQ) {
        this.socialServicesFAQ = socialServicesFAQ;
    }

    public String getWaterSanitationFAQ() {
        return waterSanitationFAQ;
    }

    public void setWaterSanitationFAQ(String waterSanitationFAQ) {
        this.waterSanitationFAQ = waterSanitationFAQ;
    }
}
