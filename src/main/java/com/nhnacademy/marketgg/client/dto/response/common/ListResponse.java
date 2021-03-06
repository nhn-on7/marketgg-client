package com.nhnacademy.marketgg.client.dto.response.common;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ListResponse<T> extends CommonResponse {

    private List<T> data;

}
