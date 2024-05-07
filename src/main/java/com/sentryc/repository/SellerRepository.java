package com.sentryc.repository;

import com.sentryc.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID>, JpaSpecificationExecutor<Seller> {

}
