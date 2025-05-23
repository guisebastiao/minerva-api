package com.minerva.minervaapi.controllers.dtos;

public record PagingDTO(
        long totalItems,
        long totalPages,
        long currentPage,
        long itemsPerPage
) { }
