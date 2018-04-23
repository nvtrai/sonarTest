package com.s3corp.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import com.s3corp.model.data.Employee;
import com.s3corp.services.EmployeeService;

@ManagedBean
public class RegisterEmployee implements Serializable{

	@ManagedProperty("#{employeeService}")
	private EmployeeService employeeService;
	
	private Employee employee = new Employee();
	private Employee selectedEmployee;
    private List<Employee> selectedEmployees;
    
    private String datePattern = "dd/MM/yyyy";
    
	private List<Employee> employeeList;

	@PostConstruct
	public void init() {
		setEmployeeList(employeeService.getEmployees(1));
	}
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String register() {
		// Calling Business Service
		employeeService.register(employee,false);
		
		setEmployeeList(employeeService.getEmployees(1));
		
		// getEmployeeList().add(employee);
		// Add message
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage("The Employee " + this.employee.getEmployeeName()+" Is Registered Successfully"));
		employee.setEmployeeHireDate(null);
		employee.setEmployeeId(0);
		employee.setEmployeeName(null);
		employee.setEmployeeSalary(0);
		return "";
	}
	
	public String delete() {
		
		if(employeeService.delete(selectedEmployees)) {
			/*for(Employee emp : selectedEmployees) {
				Iterator<Employee> itor =  employeeList.iterator();
				while(itor.hasNext()) {
					Employee eto = itor.next();
					if(emp.equals(eto)) {
						itor.remove();
						break;
					}
				}
			}*/
			setEmployeeList(employeeService.getEmployees(1));
		}
		
		return "";
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<Employee> getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(List<Employee> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

}
