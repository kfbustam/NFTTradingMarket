package com.example.restservice;

import com.example.restservice.NFT;

import com.example.restservice.nft.NftType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.codec.digest.Crypt;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

/**
 * The type Wallet.
 */
@Entity
public class Wallet {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="id", strategy = "uuid")
	private String id; // primary key

	private String origin;
	private String destination;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	private NftType nftType;

	private String imageUrl;

	private String description;
	
	@OneToMany(targetEntity=NFT.class, mappedBy="wallet")
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
     * @param passenger the passenger
     * @param flights   the flights
     */
    public Wallet(List<CryptoCurrency> cryptoCurrencies, List<NFT> nfts) {
		  this.nfts = nfts;
      this.cryptoCurrencies = cryptoCurrencies;
	  }

		public String getID() {
			return this.id;
		}

		public User getUser() {
			return this.user;
		}

	  public String getName() {
			if (this.nftType == NftType.BTC) {
				return "Bitcoin Wallet";
			} else if (this.nftType == NftType.ETH) {
				return "Ethereum Wallet";
			}
			return "NFT Wallet";
		}
	
		public String getImageUrl() {
			return this.imageUrl;
		}
		public String getDescription() {
			return this.description;
		}

		public List<CryptoCurrency> getCryptoCurrencies() {
			return this.cryptoCurrencies;
		}

		public List<NFT> getNFTs() {
			return this.nfts;
		}
}
