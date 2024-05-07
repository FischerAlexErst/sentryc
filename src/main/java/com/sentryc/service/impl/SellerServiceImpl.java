package com.sentryc.service.impl;

import com.sentryc.dto.*;
import com.sentryc.model.Marketplace;
import com.sentryc.model.Producer;
import com.sentryc.model.Seller;
import com.sentryc.model.SellerInfo;
import com.sentryc.repository.SellerRepository;
import com.sentryc.service.SellerService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public SellerPageableResponse getSellers(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy) {
        return this.findByFilter(sellerFilter, pageInput, sellerSortBy);
    }

    private SellerPageableResponse findByFilter(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy) {
        Specification<Seller> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Seller, SellerInfo> sellerInfoEntityJoin = root.join("sellerInfo");
            Join<SellerInfo, Marketplace> marketplaceEntityJoin = sellerInfoEntityJoin.join("marketplace");
            Join<Seller, Producer> producerEntityJoin = root.join("producer");

            if (sellerFilter.searchByName() != null) {
                predicates.add(cb.equal(sellerInfoEntityJoin.get("name"), sellerFilter.searchByName()));
            }

            if (!CollectionUtils.isEmpty(sellerFilter.marketplaceIds())) {
                CriteriaBuilder.In<String> inClause = cb.in(marketplaceEntityJoin.get("id"));
                for (String marketplaceId : sellerFilter.marketplaceIds()) {
                    inClause.value(marketplaceId);
                }
                predicates.add(inClause);
            }

            if (!CollectionUtils.isEmpty(sellerFilter.producerIds())) {
                CriteriaBuilder.In<UUID> inClause = cb.in(producerEntityJoin.get("id"));
                for (String producerId : sellerFilter.producerIds()) {
                    inClause.value(UUID.fromString(producerId));
                }
                predicates.add(inClause);
            }

            if (sellerSortBy != null) {
                Order order = switch (sellerSortBy) {
                    case NAME_ASC -> cb.asc(sellerInfoEntityJoin.get("name"));
                    case NAME_DESC -> cb.desc(sellerInfoEntityJoin.get("name"));
                    case MARKETPLACE_ID_ASC -> cb.asc(marketplaceEntityJoin.get("id"));
                    case MARKETPLACE_ID_DESC -> cb.desc(marketplaceEntityJoin.get("id"));
                    case SELLER_INFO_EXTERNAL_ID_ASC -> cb.asc(sellerInfoEntityJoin.get("externalId"));
                    case SELLER_INFO_EXTERNAL_ID_DESC -> cb.desc(sellerInfoEntityJoin.get("externalId"));
                };
                query.orderBy(order);
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Seller> sellers = sellerRepository.findAll(spec,
                PageRequest.of(pageInput.page(), pageInput.size()));
        var sellerPageableResponse = new SellerPageableResponse(sellers
                .stream().collect(groupingBy(Seller::getSellerInfo))
                .entrySet().stream().map((entry) -> this.convertToSeller(entry.getKey(), entry.getValue()))
                .toList(), convertToPageMeta(sellers));

        return sellerPageableResponse;
    }

    private PageMeta convertToPageMeta(Page<Seller> sellers) {
        return new PageMeta(sellers.getTotalPages(), sellers.getNumberOfElements(), sellers.getNumber(), sellers.isLast(), sellers.isFirst());
    }

    private com.sentryc.dto.Seller convertToSeller(SellerInfo sellerInfo, List<Seller> sellerList) {
        return new com.sentryc.dto.Seller(sellerInfo.getName(), sellerInfo.getExternalId(),
                this.convertToProducerSellerStates(sellerList), sellerInfo.getMarketplace().getId());
    }

    private List<ProducerSellerStates> convertToProducerSellerStates(List<Seller> sellerList) {
        List<ProducerSellerStates> producerSellerStateList = new ArrayList<>();
        for (Seller seller : sellerList) {
            var producer = seller.getProducer();
            var producerSellerState = new ProducerSellerStates(producer.getId().toString(), producer.getName(),
                    seller.getState(), seller.getId().toString());
            producerSellerStateList.add(producerSellerState);
        }
        return producerSellerStateList;
    }


}
