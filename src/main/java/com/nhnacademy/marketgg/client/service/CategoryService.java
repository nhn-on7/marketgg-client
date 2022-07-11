package com.nhnacademy.marketgg.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.marketgg.client.dto.request.CategoryCreateRequest;

public interface CategoryService {

    void createCategory(final CategoryCreateRequest categoryRequest) throws JsonProcessingException;

}