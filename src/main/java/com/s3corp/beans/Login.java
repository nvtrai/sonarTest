package com.s3corp.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.s3corp.dao.LoginDAO;
import com.s3corp.utils.SessionUtils;


@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(Login.class) ;
	private static final long serialVersionUID = 1094801825228386363L;

	private String pwd;
	private String msg;
	private String user;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// validate login
	public String validateUsernamePassword() {
		LOGGER.info(">>>>>>>> validateUsernamePassword ");
		String nameUser = LoginDAO.validate(user, pwd);
//		String nameUser = "abc";
		LOGGER.info("-----nameUser : " + nameUser);
		if (nameUser!=null) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", nameUser);
			LOGGER.info("=== goto /views/admin ");
			return "/views/admin";
		} else {
			LOGGER.error(">>>>>>>> validateUsernamePassword is failed ");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Incorrect Username and Passowrd", "Please enter correct username and Password"));
			return "/views/login";
		}
	}

	// logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "/views/login";
	}
}