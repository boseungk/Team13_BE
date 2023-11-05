package com.theocean.fundering.domain.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyFundingWithdrawalResponseDTO {

    private final Long withdrawalId; // 출금 신청 id
    private final Integer withdrawalAmount;
    private final String usage;
    private final Long postId;
    private final String thumbnail;
    private final String title;
    private final Long userId;
    private final String profileImage;
    private final String nickname;
}