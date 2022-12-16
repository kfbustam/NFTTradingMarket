package com.example.restservice.nft;

import com.example.restservice.NftUserType;
import com.example.restservice.User;
import com.example.restservice.crypto.CryptoType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="transactions")
public class NftTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = NFT.class)
    @JoinColumn(name = "nft_id")
    private NFT nft;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(name="status", columnDefinition="VARCHAR(50) default 'Completed'")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CryptoType type;

    @Column(name = "timestamp")
    private Date date;

    @Column(name="amount", columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal amount;

    // equal to current balance minus nft price
    @Column(name="post_balance", columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal postPurchaseBalance;

    public Long getId() {
        return id;
    }

    public NFT getNft() {
        return nft;
    }

    public void setNft(NFT nft) {
        this.nft = nft;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CryptoType getType() {
        return type;
    }

    public void setType(CryptoType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
