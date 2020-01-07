package com.bridgelabz.fundoonotes.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User_Details")
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@NotNull
	@NotBlank
	private String firstName;

	private String lastName;

	@NotNull
	@Column(unique = true)
	private long mobilenumber;

	@NotNull
	private String password;

	@NotNull
	@Column(unique = true)
	private String email;

	@Column(name = "created_at")
	public Date createdAt;

	@Column(name = "last_login_time")
	public Date lastLoginTime;

	public void setLastLoginTime() {
		this.lastLoginTime = new Date();
	}

	@Column(columnDefinition = "boolean default false")
	private boolean is_email_verified;

	public User(String firstName, String lastName, String email, long mobilenumber, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobilenumber = mobilenumber;
		this.password = password;

	}

	public void setCreatedAt() {
		this.createdAt = new Date();
	}

	public boolean isIs_email_verified() {
		return is_email_verified;
	}

	public void setIs_email_verified(boolean is_email_verified) {
		this.is_email_verified = is_email_verified;
	}


}
