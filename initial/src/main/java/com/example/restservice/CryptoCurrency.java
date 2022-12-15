package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * The type Passenger.
 */
@Entity
@Table(name="crypto_currency")
public class CryptoCurrency implements CryptographicAsset{

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;   // primary key

	private int price; 

	private String name;

	private String imageUrl;

	private String description;

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
  public CryptoCurrency(String name, int price, String imageUrl, String description) {
		this.price = price;
    this.name = name;
    this.imageUrl = imageUrl;
    this.description = description;
	}

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }
  public String getDescription() {
    return this.description;
  }

}
