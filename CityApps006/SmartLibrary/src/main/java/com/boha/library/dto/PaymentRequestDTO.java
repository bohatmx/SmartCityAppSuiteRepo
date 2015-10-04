package com.boha.library.dto;

/**
 * Created by aubreyM on 15/09/26.
 */
public class PaymentRequestDTO {
    private String companyID, outlet, reference,
            cardHolder, cardNumber, CCVV, email, cellphone, buyerID,
            consistentKey, redirectURL, cardType;
    private int expiryYear, expiryMonth;
    private double amount;
    private Integer profileInfoID;

    public static final int  FNB = 1;
    public static final int  NEDBANK = 2;
    public static final int  ABSA = 3;
    public static final int  STANDARD = 4;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCCVV() {
        return CCVV;
    }

    public void setCCVV(String CCVV) {
        this.CCVV = CCVV;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getConsistentKey() {
        return consistentKey;
    }

    public void setConsistentKey(String consistentKey) {
        this.consistentKey = consistentKey;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
