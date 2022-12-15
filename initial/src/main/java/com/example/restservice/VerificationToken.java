package com.example.restservice;


import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The type VerificationToken.
 */
@Entity
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String token;
  
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    
    private Date expiryDate;
   
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken() {}

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}