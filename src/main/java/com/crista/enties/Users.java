package com.crista.enties;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "firstname")
	private String firstname;
	@Email(message = "Username needs to be an email")
	@NotBlank(message = "email is required")
	@Column(name = "email")
	//@JsonIgnore
	private String email;
	//@JsonIgnore
	@Transient
	private String[] permission;
	//@JsonIgnore
	@Column(name = "userstatus")
	private String userstatus;
	//@JsonIgnore
	@Transient
	private String[] role;
	//@JsonIgnore
	//@Column(name = "type")
	@JsonIgnore
	@Transient
	private String type;
	//@JsonIgnore
	@Column(name = "token")
	private String token;
	//@JsonIgnore
	@NotBlank(message = "Password field is required")
	@Column(name = "password")
	//@JsonIgnore
	private String password;
	//@JsonIgnore
	@Transient
	private String confirmPassword;
	//@JsonIgnore
	@Column(name = "userdate")
	private Date userdate;
	@NotBlank(message = "lastname field is required")
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "vendorname")
	@NotBlank(message = "vendorname field is required")
	private String vendorname;	
	//@JsonIgnore
	@Column(name="vendorowner")
	private boolean vendorowner ;
  
	@PrePersist
	protected void onCreate() {
		this.userdate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.userdate = new Date();
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", firstname=" + firstname + ", email=" + email + ", permission="
				+ Arrays.toString(permission) + ", userstatus=" + userstatus + ", role=" + Arrays.toString(role)
				+ ", type=" + type + ", token=" + token + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", userdate=" + userdate + ", lastname=" + lastname + ", vendorname=" + vendorname
				+ ", vendorowner=" + vendorowner + "]";
	}

	
}
