
package com.boha.cityapps.data.dto;

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
    private Integer customerTypeID, countryID;
    private String iDNumber;
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
    private List<String> emailList;
    private Date dateActivated;
    private String empName;
    private String custStatus;
    private Integer capUserID;
    private Date capDate;
    private String password;
    private List<ComplaintDTO> complaintList;
    private Integer municipalityID;
    private List<AccountDTO> accountList;


    public Integer getCustomerTypeID() {
        return customerTypeID;
    }

    public void setCustomerTypeID(Integer customerTypeID) {
        this.customerTypeID = customerTypeID;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public Integer getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Integer municipalityID) {
        this.municipalityID = municipalityID;
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


    public String getIDNumber() {
        return iDNumber;
    }

    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
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
