package com.sentryc.service;

import com.sentryc.dto.PageInput;
import com.sentryc.dto.SellerFilter;
import com.sentryc.dto.SellerPageableResponse;
import com.sentryc.dto.SellerSortBy;

public interface SellerService {
    SellerPageableResponse getSellers(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy);

}
