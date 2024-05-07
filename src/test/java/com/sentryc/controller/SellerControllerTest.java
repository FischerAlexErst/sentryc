package com.sentryc.controller;

import com.sentryc.dto.*;
import com.sentryc.enums.SellerState;
import com.sentryc.model.Marketplace;
import com.sentryc.model.Producer;
import com.sentryc.model.Seller;
import com.sentryc.model.SellerInfo;
import com.sentryc.repository.MarketplaceRepository;
import com.sentryc.repository.ProducerRepository;
import com.sentryc.repository.SellerInfoRepository;
import com.sentryc.repository.SellerRepository;
import com.sentryc.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
class SellerControllerTest {

    @Autowired
    public SellerRepository sellerRepository;

    @Autowired
    public MarketplaceRepository marketplaceRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerController sellerController;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeEach
    public void init() {
        marketplaceRepository.deleteAll();
        sellerInfoRepository.deleteAll();
        producerRepository.deleteAll();
        sellerRepository.deleteAll();

        var marketplace = new Marketplace();
        marketplace.setId("marketplace1");
        marketplace.setDescription("description1");

        var sellerInfo1 = new SellerInfo();
        sellerInfo1.setName("sellerInfo1");
        sellerInfo1.setCountry("US");
        sellerInfo1.setUrl("url1");
        sellerInfo1.setExternalId("id1");
        sellerInfo1.setMarketplace(marketplace);

        var sellerInfo2 = new SellerInfo();
        sellerInfo2.setName("sellerInfo2");
        sellerInfo2.setCountry("US");
        sellerInfo2.setUrl("url2");
        sellerInfo2.setExternalId("id2");
        sellerInfo2.setMarketplace(marketplace);

        var producer1 = new Producer();
        producer1.setName("producer_name_1");
        producer1.setCreatedAt(Timestamp.from(Instant.now()));

        var producer2 = new Producer();
        producer2.setName("producer_name_2");
        producer2.setCreatedAt(Timestamp.from(Instant.now()));

        var seller1 = new Seller();
        seller1.setId(UUID.randomUUID());
        seller1.setSellerInfo(sellerInfo1);
        seller1.setProducer(producer1);
        seller1.setState(SellerState.BLACKLISTED);

        var seller2 = new Seller();
        seller2.setId(UUID.randomUUID());
        seller2.setSellerInfo(sellerInfo1);
        seller2.setProducer(producer2);
        seller2.setState(SellerState.WHITELISTED);


        marketplaceRepository.save(marketplace);
        sellerInfoRepository.saveAll(List.of(sellerInfo1, sellerInfo2));
        producerRepository.saveAll(List.of(producer1, producer2));
        sellerRepository.saveAll(List.of(seller1, seller2));

    }

    @Test
    void searchByNameFilterTest() {
        String sellerName = "sellerInfo1";
        var sellerFilter = new SellerFilter(sellerName, null, null);
        var page = new PageInput(0, 1);
        var sort = SellerSortBy.NAME_DESC;
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(sellerFilter, page, sort);

        assertEquals(sellerName, sellerPageableResponse.data().get(0).sellerName());
    }

    @Test
    void searchByMarketplaceIdFilterTest() {
        String marketPlaceId = "marketplace1";
        var searchFilter = new SellerFilter(null, null, List.of(marketPlaceId));
        var page = new PageInput(0, 1);
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(searchFilter, page, null);

        assertEquals(marketPlaceId, sellerPageableResponse.data().get(0).marketplaceId());
    }

    @Test
    void searchByProducerIdFilterTest() {
        Producer producer = producerRepository.findAll().get(0);
        List<String> stringProducerIds = List.of(producer.getId().toString());
        var searchFilter = new SellerFilter(null, stringProducerIds, null);
        var page = new PageInput(0, 2);
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(searchFilter, page, null);
        List<List<String>> result = sellerPageableResponse.data().stream().map(
                response -> response.producerSellerStates().stream().map(ProducerSellerStates::producerId).toList()
        ).toList();

        assertEquals(stringProducerIds, result.get(0));
    }

}