package com.example.restservice.nft;

import com.example.restservice.Listing;
import com.example.restservice.Offer;
import com.example.restservice.Wallet;
import com.example.restservice.crypto.CryptoType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The type Flight.
 */
@Entity
@Table(name="nft")
public class NFT {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id; // primary key

	private String tokenId;

	private String smartContractAddress;

	private String name;

	private String imageUrl;

	private String assetUrl;

	private Date lastRecordedTime;

	private String description;

	private CryptoType nftType;

	private double price;

	@OneToMany
	private List<Offer> offers;     // Full form only

	@OneToOne
	@JoinColumn(name = "listing_id")
	@Nullable
	private Listing listing;     // Full form only

	@ManyToOne
	@JoinColumn(name = "wallet_id")
	@JsonIgnore
	private Wallet wallet;     // Full form only

  	public NFT() {}


  	public NFT(int price) {
		this.price = price;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public Date getLastRecordedTime() {
		return lastRecordedTime;
	}

	public void setLastRecordedTime(Date lastRecordedTime) {
		this.lastRecordedTime = lastRecordedTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CryptoType getNftType() {
		return nftType;
	}

	public void setNftType(CryptoType nftType) {
		this.nftType = nftType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
}
