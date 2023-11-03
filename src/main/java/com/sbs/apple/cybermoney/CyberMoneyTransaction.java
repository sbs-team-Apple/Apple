package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class CyberMoneyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private SiteUser senderUser;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private SiteUser recipientUser;

    private int amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    private boolean accepted = false;

    private boolean rejected = false;
}
