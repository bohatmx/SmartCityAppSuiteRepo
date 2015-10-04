package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/09/26.
 */
public class CardResponseDTO implements Serializable {
    private String outcome, responseIndicator,
            date, time, orderID, url, cardType,
            merchantReference, consistentKey;
    private Integer cardResponseID, profileInfoID;
    private Double amount;
    private Long dateRegistered;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getCardResponseID() {
        return cardResponseID;
    }

    public void setCardResponseID(Integer cardResponseID) {
        this.cardResponseID = cardResponseID;
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getResponseIndicator() {
        return responseIndicator;
    }

    public void setResponseIndicator(String responseIndicator) {
        this.responseIndicator = responseIndicator;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getConsistentKey() {
        return consistentKey;
    }

    public void setConsistentKey(String consistentKey) {
        this.consistentKey = consistentKey;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}