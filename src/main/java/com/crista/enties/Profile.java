package com.crista.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="profile")
@Data
public class Profile {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="dob")
	private Date dob ;
	@Column(name="mobilenumber")
	private String mobilenumber;
	@Column(name="userid")
	private String userid ;
	@Column(name="gender")
	private String gender ;
	@Column(name="profilepicture")
	private String profilepicture;
	@Column(name="address")
	private String address;
	@Column(name="location")
	private String location ;	
	@PrePersist
	protected void onCreate() {
		this.dob = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.dob = new Date();
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", dob=" + dob + ", mobilenumber=" + mobilenumber + ", userid=" + userid
				+ ", gender=" + gender + ", profilepicture=" + profilepicture + ", address=" + address + ", location="
				+ location + "]";
	}
}
