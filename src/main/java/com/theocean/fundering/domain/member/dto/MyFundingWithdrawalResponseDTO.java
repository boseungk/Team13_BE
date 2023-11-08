package com.theocean.fundering.domain.member.dto;

import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.withdrawal.domain.Withdrawal;
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

    private MyFundingWithdrawalResponseDTO(Withdrawal withdrawal, Post post){
        withdrawalId = withdrawal.getWithdrawalId();
        withdrawalAmount = withdrawal.getWithdrawalAmount();
        usage = withdrawal.getUsage();
        postId = post.getPostId();
        thumbnail = post.getThumbnail();
        title = post.getTitle();
        userId = post.getWriter().getUserId();
        profileImage = post.getWriter().getProfileImage();
        nickname = post.getWriter().getNickname();
    }

    public static MyFundingWithdrawalResponseDTO of(Withdrawal withdrawal, Post post) {
        return new MyFundingWithdrawalResponseDTO(withdrawal, post);
    }
}