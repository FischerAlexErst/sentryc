package com.sentryc.repository;

import com.sentryc.model.Marketplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace, String> {
}
