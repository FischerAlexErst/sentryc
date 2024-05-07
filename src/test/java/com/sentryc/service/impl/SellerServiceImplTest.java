package com.sentryc.service.impl;


import com.sentryc.dto.PageInput;
import com.sentryc.dto.SellerFilter;
import com.sentryc.dto.SellerPageableResponse;
import com.sentryc.dto.SellerSortBy;
import com.sentryc.enums.SellerState;
import com.sentryc.model.Marketplace;
import com.sentryc.model.Producer;
import com.sentryc.model.Seller;
import com.sentryc.model.SellerInfo;
import com.sentryc.repository.SellerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class SellerServiceImplTest {
    public static final String STRING_VALUE = "string_value";
    @Mock
    SellerRepository sellerRepository;

    @InjectMocks
    SellerServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getSellersTest() {
        Marketplace marketplace = new Marketplace();
        marketplace.setId(STRING_VALUE);
        marketplace.setDescription(STRING_VALUE);

        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setId(UUID.randomUUID());
        sellerInfo.setUrl(STRING_VALUE);
        sellerInfo.setName(STRING_VALUE);
        sellerInfo.setCountry(STRING_VALUE);
        sellerInfo.setExternalId(STRING_VALUE);
        sellerInfo.setMarketplace(marketplace);

        Producer producer = new Producer();
        producer.setId(UUID.randomUUID());
        producer.setName(STRING_VALUE);
        producer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Seller seller = new Seller();
        seller.setId(UUID.randomUUID());
        seller.setState(SellerState.BLACKLISTED);
        seller.setSellerInfo(sellerInfo);
        seller.setProducer(producer);

        PageImpl<Seller> pageResult = new PageImpl<>(List.of(seller), PageRequest.of(0, 10), 10);
        SellerFilter sellerFilter = new SellerFilter("searching_name", List.of("producerId"), List.of("marketplaceId"));
        Mockito.when(sellerRepository.findAll(Mockito.<Specification<Seller>>any(), Mockito.any(PageRequest.class))).thenReturn(pageResult);
        SellerPageableResponse result = service.getSellers(sellerFilter, new PageInput(0, 10), SellerSortBy.NAME_DESC);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.meta());
        Assertions.assertNotNull(result.data());
        Assertions.assertEquals(1, result.data().size());

    }


}