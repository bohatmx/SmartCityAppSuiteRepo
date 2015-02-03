/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ProfileInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer profileInfoID;
    private int customerID;
    private String customerType;
    private String iDNumber;
    private String iDCountry;
    private String title;
    private String firstName;
    private String lastName;
    private String cellNumber;
    private String homeNumber;
    private String workNumber;
    private Boolean manAgent;
    private String orgName;
    private String contactPerson;
    private String contactPos;
    private Integer govDepID;
    private String govBranch;
    private String email1;
    private String email1Status;
    private String email2;
    private Date dateActivated;
    private String empName;
    private String custStatus;
    private Integer capUserID;
    private Date capDate;
    private String secondaryEmail;
    private String password, cityName;
    private List<ComplaintDTO> complaintList;
    private Integer cityID;
    private List<AccountDTO> accountList;

    public ProfileInfoDTO() {
    }

    public ProfileInfoDTO(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public Integer getProfileInfoID() {
        return profileInfoID;
    }

    public void setProfileInfoID(Integer profileInfoID) {
        this.profileInfoID = profileInfoID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIDNumber() {
        return iDNumber;
    }

    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getIDCountry() {
        return iDCountry;
    }

    public void setIDCountry(String iDCountry) {
        this.iDCountry = iDCountry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public Boolean getManAgent() {
        return manAgent;
    }

    public void setManAgent(Boolean manAgent) {
        this.manAgent = manAgent;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPos() {
        return contactPos;
    }

    public void setContactPos(String contactPos) {
        this.contactPos = contactPos;
    }

    public Integer getGovDepID() {
        return govDepID;
    }

    public void setGovDepID(Integer govDepID) {
        this.govDepID = govDepID;
    }

    public String getGovBranch() {
        return govBranch;
    }

    public void setGovBranch(String govBranch) {
        this.govBranch = govBranch;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail1Status() {
        return email1Status;
    }

    public void setEmail1Status(String email1Status) {
        this.email1Status = email1Status;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public Date getDateActivated() {
        return dateActivated;
    }

    public void setDateActivated(Date dateActivated) {
        this.dateActivated = dateActivated;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public Integer getCapUserID() {
        return capUserID;
    }

    public void setCapUserID(Integer capUserID) {
        this.capUserID = capUserID;
    }

    public Date getCapDate() {
        return capDate;
    }

    public void setCapDate(Date capDate) {
        this.capDate = capDate;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ComplaintDTO> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintDTO> complaintList) {
        this.complaintList = complaintList;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getiDCountry() {
        return iDCountry;
    }

    public void setiDCountry(String iDCountry) {
        this.iDCountry = iDCountry;
    }


    public List<AccountDTO> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDTO> accountList) {
        this.accountList = accountList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profileInfoID != null ? profileInfoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfileInfoDTO)) {
            return false;
        }
        ProfileInfoDTO other = (ProfileInfoDTO) object;
        if ((this.profileInfoID == null && other.profileInfoID != null) || (this.profileInfoID != null && !this.profileInfoID.equals(other.profileInfoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.ProfileInfo[ profileInfoID=" + profileInfoID + " ]";
    }
    
}
