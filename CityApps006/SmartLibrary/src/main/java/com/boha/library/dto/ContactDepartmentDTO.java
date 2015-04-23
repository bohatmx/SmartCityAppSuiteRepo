/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ContactDepartmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer contactDepartmentID;
    private String contactDepartmentName;
    private List<ContactDTO> contactList;

    public ContactDepartmentDTO() {
    }


    public Integer getContactDepartmentID() {
        return contactDepartmentID;
    }

    public void setContactDepartmentID(Integer contactDepartmentID) {
        this.contactDepartmentID = contactDepartmentID;
    }

    public String getContactDepartmentName() {
        return contactDepartmentName;
    }

    public void setContactDepartmentName(String contactDepartmentName) {
        this.contactDepartmentName = contactDepartmentName;
    }

    public List<ContactDTO> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactDTO> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactDepartmentID != null ? contactDepartmentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactDepartmentDTO)) {
            return false;
        }
        ContactDepartmentDTO other = (ContactDepartmentDTO) object;
        if ((this.contactDepartmentID == null && other.contactDepartmentID != null) || (this.contactDepartmentID != null && !this.contactDepartmentID.equals(other.contactDepartmentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.smartcity.data.ContactDepartment[ contactDepartmentID=" + contactDepartmentID + " ]";
    }
    
}
