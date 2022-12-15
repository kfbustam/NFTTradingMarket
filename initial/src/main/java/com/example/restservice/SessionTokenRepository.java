package com.example.restservice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Session repository.
 */
public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {
    /**
     * Find by token optional.
     *
     * @param String the token
     * @return the optional
     */
    Optional<SessionToken> findById(String id);

    Optional<SessionToken> findByToken(String token);
}