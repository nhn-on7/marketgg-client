package com.nhnacademy.marketgg.client.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostResponse {

    private Long boardNo;

    private String categoryCode;

    private String title;

    private String reason;

    private String status;

    private LocalDateTime createdAt;

}
