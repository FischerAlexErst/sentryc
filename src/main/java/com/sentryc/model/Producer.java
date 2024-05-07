package com.sentryc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "producers", schema = "sentryc_interview")
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "producer")
    private Seller seller;

}
