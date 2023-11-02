package com.theocean.fundering.domain.myfunding.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MyFundingResponseDTO {
    private Long postId;
    private Long writerId;
    private String writer;
    private String celebrity;
    private String celebImg;
    private String title;
    private String thumbnail;
    private int targetPrice;
    private int currentAmount;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
