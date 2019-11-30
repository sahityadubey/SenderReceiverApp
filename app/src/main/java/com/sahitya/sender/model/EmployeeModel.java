package com.sahitya.sender.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EmployeeModel {

    private String EmpDocId;
    private List<EmployeeDetail> EmpDetails;
    private String LastModifiedTimeUTC;
    private boolean isShouldSynced;

    public EmployeeModel() {
    }
    public EmployeeModel(List<EmployeeDetail> EmpDetails) {
        this.EmpDetails = EmpDetails;
    }

    public EmployeeModel(String EmpDocId, List<EmployeeDetail> EmpDetails, String LastModifiedTimeUTC, boolean isShouldSynced) {
        this.EmpDocId = EmpDocId;
        this.EmpDetails = EmpDetails;
        this.LastModifiedTimeUTC = LastModifiedTimeUTC;
        this.isShouldSynced = isShouldSynced;
    }
    public String getEmpDocId() {
        return EmpDocId;
    }

    public void setEmpDocId(String empDocId) {
        EmpDocId = empDocId;
    }

    public List<EmployeeDetail> getEmpDetails() {
        return EmpDetails;
    }

    public void setEmpDetails(List<EmployeeDetail> empDetails) {
        EmpDetails = empDetails;
    }

    public String getLastModifiedTimeUTC() {
        return LastModifiedTimeUTC;
    }

    public void setLastModifiedTimeUTC(String lastModifiedTimeUTC) {
        LastModifiedTimeUTC = lastModifiedTimeUTC;
    }

    public boolean isShouldSynced() {
        return isShouldSynced;
    }

    public void setIsShouldSynced(boolean synced) {
        isShouldSynced = synced;
    }
}
