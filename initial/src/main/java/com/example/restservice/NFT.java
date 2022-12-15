package com.example.restservice;

import com.example.restservice.nft.NftType;
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
	//Token ID
	//Smart Contract Address
	//Name
	//Type
	//Description
	//Image URL
	//Asset URL
	//LastRecordedTime
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

	private String tokenId;

	private String smartContractAddress;

	private String name;

	private String imageUrl;

	private String assetUrl;

	private Date lastRecordedTime;

	private String description;

	private NftType nftType;

	private double price;

	@ManyToOne
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;     // Full form only

	/**
	* Instantiates a new Flight.
	*/
  	public NFT() {}


  	public NFT(int price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public NftType getNftType() {
		return nftType;
	}

	public void setNftType(NftType nftType) {
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
