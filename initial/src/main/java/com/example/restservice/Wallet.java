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

}
