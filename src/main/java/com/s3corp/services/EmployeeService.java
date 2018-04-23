package com.s3corp.services;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.s3corp.dao.JdbcUtils;
import com.s3corp.model.data.Employee;
import com.s3corp.utils.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class EmployeeService implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(EmployeeService.class);

	public void register(Employee emp) {
		// Acquire session
		JdbcUtils jdbcUtils = new JdbcUtils();
		String sqlInsert = "INSERT INTO s3corp.employee(emp_name,emp_hire_date,emp_salary) VALUES (?,?,?)";

		Object[] params = { emp.getEmployeeName(), emp.getEmployeeHireDate(), emp.getEmployeeSalary() };
		try {
			jdbcUtils.executeUpdate(sqlInsert, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			jdbcUtils.close();
		}

	}

	public List<Employee> getEmployees() {
		List<Employee> list = new ArrayList<>();

		JdbcUtils jdbcUtils = new JdbcUtils();
		String sqlSelect = "SELECT ID as id, EMP_NAME as name, EMP_HIRE_DATE as date, EMP_SALARY as salary FROM S3CORP.EMPLOYEE ";

		ResultSet rs;
		try {
			rs = jdbcUtils.select(sqlSelect, null);
			while (rs.next()) {
				Employee emp = new Employee();
				emp.setEmployeeHireDate(rs.getDate("date"));
				emp.setEmployeeId(rs.getLong("id"));
				emp.setEmployeeName(rs.getString("name"));
				emp.setEmployeeSalary(rs.getDouble("salary"));
				list.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	public Boolean delete(List<Employee> selectedEmployees) {

		if (CollectionUtils.isEmpty(selectedEmployees))
			return false;

		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM s3corp.employee WHERE ID IN (");

		Object[] params = new Long[selectedEmployees.size()];

		for (int i = 0; i < selectedEmployees.size(); i++) {
			params[i] = selectedEmployees.get(i).getEmployeeId();
			sqlBuilder.append("?,");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(")");

		JdbcUtils jdbcUtils = new JdbcUtils();

		return jdbcUtils.executeQuery(sqlBuilder.toString(), params);
	}

	public List<Employee> getEmployees(int size) {

		LOGGER.info("........getEmpoyee by size ");

		List<Employee> list = new ArrayList<>();

		SessionFactory sessFact = HibernateUtil.getSessionFactory();
		Session session = sessFact.getCurrentSession();
		org.hibernate.Transaction tr = session.beginTransaction();

		CriteriaQuery cq = session.getCriteriaBuilder().createQuery(com.s3corp.domain.Employee.class);
		cq.from(com.s3corp.domain.Employee.class);
		List<com.s3corp.domain.Employee> employeeList = session.createQuery(cq).getResultList();

		for (com.s3corp.domain.Employee employee : employeeList) {

			LOGGER.info("........convert domain.Employee to model.data.Employee, id = " + employee.getId());

			Employee emp = new Employee();
			emp.setEmployeeId(employee.getId());
			emp.setEmployeeHireDate(employee.getEmpHireDate());
			emp.setEmployeeName(employee.getEmpName());
			emp.setEmployeeSalary(employee.getEmpSalary().doubleValue());
			list.add(emp);
		}

		tr.commit();
		return list;

	}

	public void register(Employee emp, Boolean isBatch) {
		SessionFactory sessFact = HibernateUtil.getSessionFactory();
		Session session = sessFact.getCurrentSession();
		org.hibernate.Transaction tr = session.beginTransaction();
		
		com.s3corp.domain.Employee employee = new com.s3corp.domain.Employee();
		employee.setEmpHireDate(emp.getEmployeeHireDate());
		employee.setEmpName(emp.getEmployeeName());
		employee.setEmpSalary(emp.getEmployeeSalary());
		employee.setId(emp.getEmployeeId());
		
		session.save(employee);
		
		tr.commit();

	}
	
	public Boolean delete(List<Employee> selectedEmployees,boolean isHibernate) {
		
		if (CollectionUtils.isEmpty(selectedEmployees))
			return false;

		SessionFactory sessFact = HibernateUtil.getSessionFactory();
		Session session = sessFact.getCurrentSession();
		org.hibernate.Transaction tr = session.beginTransaction();
		
		
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM s3corp.employee WHERE emp_id IN (");

		Object[] params = new Long[selectedEmployees.size()];

		for (int i = 0; i < selectedEmployees.size(); i++) {
			params[i] = selectedEmployees.get(i).getEmployeeId();
			sqlBuilder.append("?,");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		sqlBuilder.append(")");

	
		
		javax.persistence.Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(1, params);
		
		query.executeUpdate();
		
		tr.commit();
		
		return true;
	}


}
