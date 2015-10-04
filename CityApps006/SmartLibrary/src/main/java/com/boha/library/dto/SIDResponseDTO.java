package com.boha.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/10/01.
 */
public class SIDResponseDTO implements Serializable{
    private String status, merchant,country,currency,reference,bank,date, receiptNumber, transactionID, consistentKey;
    private Double amount;
    private Long dateRegistered;

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
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
