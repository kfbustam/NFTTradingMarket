package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The type User.
 */
@Entity
public class User {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

  @NotNull(message="{email.required}")
  @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
          + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
          + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
          + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?",
          message = "{invalid.email}")
  @Column(unique = true)
	private String username;

  @Pattern(regexp = "[A-Za-z0-9]+",
          message = "{invalid.email}")
  @Column(unique = true)
	private String nickname;
	// private int birthyear;  // Full form only (see definition below)
	// private String gender;  // Full form only
	// private String phone; // Phone numbers must be unique.   Full form only

	// @OneToMany(targetEntity=Wallet.class, mappedBy="passenger", fetch=FetchType.EAGER)
	// private List<Wallet> reservations;   // Full form only

	// @ManyToOne
	// @JoinColumns({
	// 	@JoinColumn(name = "flightNumber", insertable = false, updatable = false),
	// 	@JoinColumn(name = "departureDate", insertable = false, updatable = false)
	// })
	// private NFT flight;

  /**
   * Instantiates a new User.
   */
  public User() {}

  /**
   * Instantiates a new User.
   *
   * @param username a valid email address
   * @param nickname the nickname
   */
  public User(String username, String nickname) {
		this.username = username;
		this.nickname = nickname;
	}
}
