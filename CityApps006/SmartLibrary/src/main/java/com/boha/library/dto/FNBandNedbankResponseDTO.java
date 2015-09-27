package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/09/26.
 */
public class FNBandNedbankResponseDTO implements Serializable{
    String transactionOutcome, responseIndicator,
            merchantReference;
    Long transactionDate;
    String transactionOrderID, date,time;
    Double transactionAmount;

    public String getTransactionOutcome() {
        return transactionOutcome;
    }

    public void setTransactionOutcome(String transactionOutcome) {
        this.transactionOutcome = transactionOutcome;
    }

    public String getResponseIndicator() {
        return responseIndicator;
    }

    public void setResponseIndicator(String responseIndicator) {
        this.responseIndicator = responseIndicator;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionOrderID() {
        return transactionOrderID;
    }

    public void setTransactionOrderID(String transactionOrderID) {
        this.transactionOrderID = transactionOrderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
