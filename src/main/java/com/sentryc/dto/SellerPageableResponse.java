package com.sentryc.dto;

import java.util.List;

public record SellerPageableResponse(List<Seller> data, PageMeta meta) {
}
