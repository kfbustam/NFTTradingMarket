package com.example.restservice;

import com.example.restservice.crypto.CryptoType;

import javax.persistence.*;

import com.example.restservice.nft.NFT;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Wallet.
 */
@Entity
@Table(name="wallet")
public class Wallet {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id; // primary key

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "type")
	private CryptoType type;

	@Column(name="balance", columnDefinition="Decimal(10,2) default '0.00'")
	private BigDecimal cryptoBalance;

	@OneToMany(targetEntity= NFT.class, mappedBy="wallet")
	private List<NFT> nfts;

	/**
	 * Instantiates a new Wallet.
	 */
	public Wallet() {}

	/**
	 * Instantiates a new Wallet.
	 *
	 * @param nfts             the nfts
	 */
	public Wallet(List<NFT> nfts) {
		  this.nfts = nfts;
	  }

	public Wallet(User user, CryptoType type) {
		this.user = user;
		this.type = type;
		this.cryptoBalance = BigDecimal.valueOf(0.00);
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

	public BigDecimal getCryptoBalance() {
		return cryptoBalance;
	}

	public void setCryptoBalance(BigDecimal cryptoBalance) {
		this.cryptoBalance = cryptoBalance;
	}
}
