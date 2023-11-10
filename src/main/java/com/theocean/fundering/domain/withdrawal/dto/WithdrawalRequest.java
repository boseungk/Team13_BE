package com.theocean.fundering.domain.withdrawal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WithdrawalRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SaveDTO {
        @NotBlank(message = "사용처 입력은 필수입니다.")
        private String usage;

        @NotBlank(message = "계좌번호를 입력해주세요.")
        private String depositAccount;

        @Min(message = "신청 금액은 최소 10000원 이상 입니다.", value = 10000)
        private int amount;
    }
}
