/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer accountID;
    private String accountNumber;
    private Long dateLastUpdated;
    private Double currentBalance;
    private Double totalAccountAmount;
    private Double cashAfterAccount;
    private String customerAccountName;
    private String propertyAddress;
    private Double lastBillAmount;
    private Long nextBillDate;
    private Double currentArrears;
    private Integer billDay;
    private Long previousBillDate;
    private Integer profileInfoID;

    public AccountDTO() {
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getTotalAccountAmount() {
        return totalAccountAmount;
    }

    public void setTotalAccountAmount(Double totalAccountAmount) {
        this.totalAccountAmount = totalAccountAmount;
    }

    public Double getCashAfterAccount() {
        return cashAfterAccount;
    }

    public void setCashAfterAccount(Double cashAfterAccount) {
        this.cashAfterAccount = cashAfterAccount;
    }

    public String getCustomerAccountName() {
        return customerAccountName;
    }

    public void setCustomerAccountName(String customerAccountName) {
        this.customerAccountName = customerAccountName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public Double getLastBillAmount() {
        return lastBillAmount;
    }

    public void setLastBillAmount(Double lastBillAmount) {
        this.lastBillAmount = lastBillAmount;
    }


    public Double getCurrentArrears() {
        return currentArrears;
    }

    public void setCurrentArrears(Double currentArrears) {
        this.currentArrears = currentArrears;
    }

    public Integer getBillDay() {
        return billDay;
    }

    public void setBillDay(Integer billDay) {
        this.billDay = billDay;
    }

    public Long getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Long dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public Long getNextBillDate() {
        return nextBillDate;
    }

    public void setNextBillDate(Long nextBillDate) {
        this.nextBillDate = nextBillDate;
    }

    public Long getPreviousBillDate() {
        return previousBillDate;
    }

    public void setPreviousBillDate(Long previousBillDate) {
        this.previousBillDate = previousBillDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountID != null ? accountID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountDTO)) {
            return false;
        }
        AccountDTO other = (AccountDTO) object;
        if ((this.accountID == null && other.accountID != null) || (this.accountID != null && !this.accountID.equals(other.accountID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.Account[ accountID=" + accountID + " ]";
    }
    
}
