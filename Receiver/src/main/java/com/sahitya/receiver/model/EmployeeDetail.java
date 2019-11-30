package com.sahitya.receiver.model;

public class EmployeeDetail {
    private String EmpName, EmpComapany, EmpExperience;

    public EmployeeDetail() {
    }

    public EmployeeDetail(String EmpName, String EmpComapany, String EmpExperience) {
        this.EmpName = EmpName;
        this.EmpComapany = EmpComapany;
        this.EmpExperience = EmpExperience;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getEmpComapany() {
        return EmpComapany;
    }

    public void setEmpComapany(String empComapany) {
        EmpComapany = empComapany;
    }

    public String getEmpExperience() {
        return EmpExperience;
    }

    public void setEmpExperience(String empExperience) {
        EmpExperience = empExperience;
    }
}
