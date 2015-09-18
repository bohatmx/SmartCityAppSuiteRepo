package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/09/17.
 */
public class PaymentSurveyDTO implements Serializable{
    private Integer municipalityID;
    private static final long serialVersionUID = 1L;
    private Integer paymentSurveyID;
    private String accountNumber;
    private Long responseDate;
    private boolean response;

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
    }

    public Integer getPaymentSurveyID() {
        return paymentSurveyID;
    }

    public void setPaymentSurveyID(Integer paymentSurveyID) {
        this.paymentSurveyID = paymentSurveyID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Long responseDate) {
        this.responseDate = responseDate;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
