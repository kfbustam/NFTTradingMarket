package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * The type Passenger.
 */
@Entity
@Table(name="crypto_currency")
public class CryptoCurrency {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;   // primary key

	private int price; 

	@ManyToOne
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;     // Full form only

  /**
   * Instantiates a new CryptoCurrency.
   */
  public CryptoCurrency() {}

  /**
   * Instantiates a new CryptoCurrency.
   *
   * @param price the price
   */
  public CryptoCurrency(int price) {
		this.price = price;

	}



}
