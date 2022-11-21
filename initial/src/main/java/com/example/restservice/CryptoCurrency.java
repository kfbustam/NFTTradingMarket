package com.example.restservice;

import com.example.restservice.NFT;

import org.springframework.boot.SpringApplication;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.persistence.*;
import java.util.List;
import com.example.restservice.*;

/**
 * The type Passenger.
 */
@Entity
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
