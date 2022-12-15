package com.example.restservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import com.example.restservice.nft.NFT;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


/**
 * An Listing for an NFT.
 */
@Entity
@Table(name="listing")
public class Listing {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id; // primary key

	@ManyToOne
	@JoinColumn(name = "seller_id")
	@JsonIgnore
	private User seller;

	@Column(name="listing_price", columnDefinition="Decimal(10,2) default '0.00'")
	private BigDecimal listingPrice;

	@JsonFormat(pattern="HH:mm:ss")
	@Nullable
	private Date expirationTime;

	@OneToOne
	@JoinColumn(name = "nft_id")
	@JsonIgnore
	private NFT nft;     // Full form only

	@Enumerated(EnumType.STRING)
	@Column(name="sale_type", columnDefinition="VARCHAR(255) default 'IMMEDIATE'")
	private SaleType type;

  public Listing() {}


  public Listing(BigDecimal listingPrice) {
		this.listingPrice = listingPrice;
	}

	public BigDecimal getListingPrice() {
		return this.listingPrice;
	}

	public Date getExpirationTime() {
		return this.expirationTime;
	}

	public NFT getNFT() {
		return this.nft;
	}

	public SaleType getType() {
		return type;
	}

	public void setType(SaleType type) {
		this.type = type;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
}
