package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The type Session.
 */
@Entity
public class SessionToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @ManyToOne(targetEntity = User.class)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  /**
   * Epoch time.
   */
	private int expirationDate;

  /**
   * Instantiates a new Session.
   */
  public SessionToken() {}

  /**
   * Instantiates a new Session.
   *
   * 
   */
  public SessionToken(User user, String token, int expirationDate) {
		this.user = user;
    this.token = token;
    this.expirationDate = expirationDate;
	}

  /**
   * Gets Session ID.
   *
   * @return the id
   */
  public Long getID() {
    return this.id;
  }

  /**
   * Gets User.
   *
   * @return the user
   */
  public User getUser() {
    return this.user;
  }

  public String getToken() {
    return this.token;
  }

  public int getExpirationDate() {
    return this.expirationDate;
  }
}
