package com.example.restservice;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface VerificationTokenRepository repository.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    /**
     * Find by verification token optional.
     *
     * @return the optional
     */
    Optional<VerificationToken> findByToken(String verificationToken);

}