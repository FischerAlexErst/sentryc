package com.sentryc.dto;

import java.util.List;

public record Seller(String sellerName,
                     String externalId,
                     List<ProducerSellerStates> producerSellerStates,
                     String marketplaceId) {
}
