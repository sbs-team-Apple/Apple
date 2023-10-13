package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private SiteUser sender;
    @ManyToOne
    private SiteUser recipient;
    private int amount;
    private LocalDateTime timestamp;

    public TransactionLog(SiteUser sender, SiteUser recipient, int amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
}

