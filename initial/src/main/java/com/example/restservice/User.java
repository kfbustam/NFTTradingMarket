package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The type User.
 */
@Entity
@Table(name="user")
public class User {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

  @NotNull(message="{email.required}")
  @NotEmpty
  @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
          + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
          + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
          + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?",
          message = "{invalid.email}")
  @Column(unique = true)
	private String email;

  @Pattern(regexp = "[A-Za-z0-9!#$%&]+", message = "{invalid.password}")
  @NotEmpty
  @Column(unique = true)
  private String password;

  @Pattern(regexp = "[A-Za-z0-9]+", message = "{invalid.firstname}")
  @NotEmpty
  @Column(unique = false)
  private String firstname;

  @Pattern(regexp = "[A-Za-z0-9]+", message = "{invalid.lastname}")
  @NotEmpty
  @Column(unique = false)
  private String lastname;

  @Pattern(regexp = "[A-Za-z0-9]+", message = "{invalid.nickname}")
  @NotEmpty
  @Column(unique = true)
	private String nickname;

	private Boolean isVerified;

  /**
   * Instantiates a new User.
   */
  public User() {}

  /**
   * Instantiates a new User.
   *
   * @param email a valid email address
   * @param password the users password
   * @param firstname the users firstname
   * @param lastname the users lastname
   * @param nickname the users nickname
   * 
   */
  public User(String email, String password, String firstname, String lastname, String nickname) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.nickname = nickname;
    this.isVerified = false;
	}

  /**
   * Gets first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return this.firstname;
  }


  /**
   * Gets last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return this.lastname;
  }

  /**
   * Gets nickname.
   *
   * @return the nickname
   */
  public String getNickName() {
  return this.lastname;
}

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Sets token.
   *
   */
  public void setVerified() {
    this.isVerified = true;
  }

  /**
   * Checks if user is verified.
   *
   */
  public Boolean isVerified() {
    return this.isVerified;
  }
}
