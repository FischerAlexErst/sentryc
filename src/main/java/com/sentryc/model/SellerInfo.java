package com.sentryc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "seller_infos", schema = "sentryc_interview")
public class SellerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "url")
    private String url;

    @Column(name = "external_id")
    private String externalId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marketplace_id", referencedColumnName = "id")
    private Marketplace marketplace;

    @OneToMany(mappedBy = "sellerInfo", cascade = CascadeType.REMOVE)
    private List<Seller> sellers;
}
