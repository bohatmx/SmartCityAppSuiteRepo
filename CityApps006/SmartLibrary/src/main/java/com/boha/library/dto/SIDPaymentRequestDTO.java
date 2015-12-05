package com.boha.library.dto;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

/**
 * Created by aubreyM on 15/10/02.
 */
public class SIDPaymentRequestDTO implements Serializable{

    public static final String SID_URL = "https://www.sidpayment.com/paySID/",
            SID_MERCHANT = "SID_MERCHANT",
            SID_CURRENCY = "SID_CURRENCY",
            SID_COUNTRY = "SID_COUNTRY",
            SID_REFERENCE = "SID_REFERENCE",
            SID_AMOUNT = "SID_AMOUNT",
            SID_CONSISTENT = "SID_CONSISTENT";

    public static final String
            SID_MERCHANT_CODE = "PHONE5",
            SID_KEY = "AdgMFTZi8tJxIP2T1XrRisQr9tFKmFJryhNAlxb5CjEWSsvMHwAoHet2ZTogFUS",
            SID_UNAME = "phone5@phonewarehouse.co.za",
            SID_PASSWORD = "KEAQ4022";

    String merchant = SID_MERCHANT_CODE,
            country = "ZA",
            reference,
            currency = "ZAR";
    Double amount;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getConsistentKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(merchant)
                .append(currency)
                .append(country)
                .append(reference)
                .append(amount.doubleValue())
                .append(SID_KEY);

        String hash = new String(Hex.encodeHex(
                DigestUtils.sha512(sb.toString())));
        return hash.toUpperCase();
    }
}
