package com.sentryc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "marketplaces", schema = "sentryc_interview")
public class Marketplace {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "marketplace", cascade = CascadeType.ALL)
    private List<SellerInfo> sellerInfo;

}