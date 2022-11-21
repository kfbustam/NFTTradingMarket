package com.example.restservice;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.restservice.CryptoCurrency;
import java.util.List;
import java.util.Optional;

/**
 * The interface Passenger repository.
 */
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, String> {

}