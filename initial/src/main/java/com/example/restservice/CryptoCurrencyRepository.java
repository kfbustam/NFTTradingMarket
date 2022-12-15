package com.example.restservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.restservice.CryptoCurrency;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface Passenger repository.
 */
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, String> {
  @Query(value="SELECT * FROM crypto_currency c WHERE c.wallet_id=?1", nativeQuery = true)
  public Collection<CryptoCurrency> findByWalletID(String wallet_id);
}