package com.s3corp.model.data;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable{
	private long employeeId;
	private String employeeName;
	private Date employeeHireDate;
	private double employeeSalary;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getEmployeeHireDate() {
		return employeeHireDate;
	}

	public void setEmployeeHireDate(Date employeeHireDate) {
		this.employeeHireDate = employeeHireDate;
	}

	public double getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(double employeeSalary) {
		this.employeeSalary = employeeSalary;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj==null) return false;
		if(!(obj instanceof Employee)) return false;
		
		Employee emp = (Employee) obj;
		if(emp.getEmployeeId() == this.getEmployeeId()) return true; 
		
		return false;
	}

}
