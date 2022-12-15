package com.example.restservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import com.example.restservice.nft.NFT;

import java.util.Date;

import javax.persistence.*;


/**
 * An Listing for an NFT.
 */
@Entity
@Table(name="listing")
public class Listing {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

	@ManyToOne
	@JoinColumn(name = "seller_id")
	@JsonIgnore
	private User seller;

	private double listingPrice;

	@JsonFormat(pattern="HH:mm:ss")
	@Nullable
	private Date expirationTime;

	@OneToOne
	@JoinColumn(name = "nft_id")
	@JsonIgnore
	private NFT nft;     // Full form only

  public Listing() {}


  public Listing(double listingPrice) {
		this.listingPrice = listingPrice;
	}

	public double getListingPrice() {
		return this.listingPrice;
	}
}
