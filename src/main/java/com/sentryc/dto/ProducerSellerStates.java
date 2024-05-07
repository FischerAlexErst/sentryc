package com.sentryc.dto;

import com.sentryc.enums.SellerState;

public record ProducerSellerStates(String producerId,
                                   String producerName,
                                   SellerState sellerState,
                                   String sellerId) {}
