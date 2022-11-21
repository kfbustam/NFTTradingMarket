package com.example.restservice;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.restservice.NFT;
import com.example.restservice.CryptoCurrency;
import java.util.Date;
import java.util.List;
import javax.persistence.EmbeddedId;

/**
 * The type Flight.
 */
@Entity
public class NFT {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key
 
	@ManyToOne
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;     // Full form only

	private int price; 

  /**
   * Instantiates a new Flight.
   */
  public NFT() {}


  public NFT(int price) {
		this.price = price;
	}

}
