package com.boha.library.rssreader;

import java.io.Serializable;

/**
 * Created by Nkululeko on 2016/08/30.
 */
public class FaqTest implements Serializable{


   public String AccountsPayments = "Accounts & Payments";
    public String WaterSanitation = "Water & Sanitation";
    public String CleansingSolidWaste = "Cleansing & Solid Waste";
    public String RatesTaxes = "Rates & Taxes";
    public String BuildingPlans = "Building Plans";
    public String Electricity = "Electricity";
    public String SocialServices = "Social Services";
    public String Health = "Health";
    public String MetroPolice = "Metro Police";
    String [] FAQ = {"Accounts & Payments", "Water & Sanitation", "Cleansing & Solid Waste",
            "Rates & Taxes", "Building Plans", "Electricity", "Social Services",
            "Health", "Metro Police"};

    public String[] getFAQ() {
        return FAQ;
    }

    public void setFAQ(String[] FAQ) {
        this.FAQ = FAQ;
    }

    public String getAccountsPayments() {
        return AccountsPayments;
    }

    public void setAccountsPayments(String accountsPayments) {
        AccountsPayments = accountsPayments;
    }

    public String getWaterSanitation() {
        return WaterSanitation;
    }

    public void setWaterSanitation(String waterSanitation) {
        WaterSanitation = waterSanitation;
    }

    public String getCleansingSolidWaste() {
        return CleansingSolidWaste;
    }

    public void setCleansingSolidWaste(String cleansingSolidWaste) {
        CleansingSolidWaste = cleansingSolidWaste;
    }

    public String getRatesTaxes() {
        return RatesTaxes;
    }

    public void setRatesTaxes(String ratesTaxes) {
        RatesTaxes = ratesTaxes;
    }

    public String getBuildingPlans() {
        return BuildingPlans;
    }

    public void setBuildingPlans(String buildingPlans) {
        BuildingPlans = buildingPlans;
    }

    public String getElectricity() {
        return Electricity;
    }

    public void setElectricity(String electricity) {
        Electricity = electricity;
    }

    public String getSocialServices() {
        return SocialServices;
    }

    public void setSocialServices(String socialServices) {
        SocialServices = socialServices;
    }

    public String getHealth() {
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getMetroPolice() {
        return MetroPolice;
    }

    public void setMetroPolice(String metroPolice) {
        MetroPolice = metroPolice;
    }
}
