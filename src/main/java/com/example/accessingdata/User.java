package com.example.accessingdata;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
//Ну коммент появился.
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users_data",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})}
)
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@Column(unique = true)
	private String login;

	private String pass;

	private String stat;

	private String firstName;

	private String lastName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getHashedPass(String pass) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] hashedPassword = md.digest(pass.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().encodeToString(hashedPassword);
		} catch (java.security.GeneralSecurityException e) {}

		return null;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}
}
