package com.sentryc.dto;

public record PageMeta(Integer totalPages, Integer totalItems, Integer currentPage, Boolean hasNextPage,
                       Boolean hasPreviousPage) {
}

