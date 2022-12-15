package com.example.restservice;

import com.example.restservice.Wallet;
import com.example.restservice.crypto.CryptoType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import com.example.restservice.nft.NFT;

import javax.persistence.*;

import java.util.Date;

/**
 * An Offer for an NFT.
 */
@Entity
@Table(name="offer")
public class Offer {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	private double offerPrice;

	@ManyToOne
	@JoinColumn(name = "nft_id")
	@JsonIgnore
	private NFT nft;     // Full form only

  public Offer() {}


  public Offer(int offerPrice) {
		this.offerPrice = offerPrice;
	}

	public double getOfferPrice() {
		return this.offerPrice;
	}
}
