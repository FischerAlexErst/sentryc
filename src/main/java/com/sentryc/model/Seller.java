package com.sentryc.model;


import com.sentryc.enums.SellerState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "sellers", schema = "sentryc_interview")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SellerState state;

    @ManyToOne
    @JoinColumn(name = "seller_info_id", referencedColumnName = "id")
    private SellerInfo sellerInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true, name = "producer_id", referencedColumnName = "id")
    private Producer producer;
}
