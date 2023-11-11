package com.theocean.fundering.global.errors.exception;

public class ErrorCode {

    // 사용자 조회 오류(찾을 수 없음)
    public static final String ER01 = "ER01";

    //셀럽 조회 오류(찾을 수 없음)
    public static final String ER02 = "ER02";

    // 게시물 조회 오류(찾을 수 없음)
    public static final String ER03 = "ER03";

    // 파일 업로드 오류(AWS S3에 업로드 할 수 없음)
    public static final String ER04 = "ER04";

    // 공동 관리자 선임 오류
    public static final String ER05 = "ER05";

    // 인증되지 않은 사용자가 로그인이 필요한 로직에 접근함
    public static final String ER06 = "ER06";

    // Request Validation 오류
    public static final String ER07 = "ER017";
}
