package com.example.restservice;

import com.example.restservice.crypto.CryptoType;

import javax.persistence.*;

import com.example.restservice.nft.NFT;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

/**
 * The type Wallet.
 */
@Entity
@Table(name="wallet")
public class Wallet {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "type")
	private CryptoType type;

	@OneToMany(targetEntity= NFT.class, mappedBy="wallet")
	private List<NFT> nfts; 

	@OneToMany(targetEntity=CryptoCurrency.class, mappedBy="wallet")
	private List<CryptoCurrency> cryptoCurrencies;

	/**
	 * Instantiates a new Wallet.
	 */
	public Wallet() {}

	/**
	 * Instantiates a new Wallet.
	 *
	 * @param cryptoCurrencies the cryptocurrencies
	 * @param nfts             the nfts
	 */
	public Wallet(List<CryptoCurrency> cryptoCurrencies, List<NFT> nfts) {
		  this.nfts = nfts;
      this.cryptoCurrencies = cryptoCurrencies;
	  }

	public Wallet(User user, CryptoType type) {
		this.user = user;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CryptoType getType() {
		return type;
	}

	public void setType(CryptoType type) {
		this.type = type;
	}

	public List<NFT> getNfts() {
		return nfts;
	}

	public void setNfts(List<NFT> nfts) {
		this.nfts = nfts;
	}

	public List<CryptoCurrency> getCryptoCurrencies() {
		return cryptoCurrencies;
	}

	public void setCryptoCurrencies(List<CryptoCurrency> cryptoCurrencies) {
		this.cryptoCurrencies = cryptoCurrencies;
	}
}
