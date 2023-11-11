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
    public static final String ER07 = "ER07";

    // 계좌 조회 오류(찾을 수 없음)
    public static final String ER08 = "ER08";

    // 댓글 조회 오류(찾을 수 없음)
    public static final String ER09 = "ER09";

    // 삭제된 댓글 조회
    public static final String ER10 = "ER10";

    // 작성자가 아닌 자가 댓글 삭제 시도
    public static final String ER11 = "ER11";

    // 대댓글에 댓글 작성 시도
    public static final String ER12 = "ER12";

    // 허용된 대댓글 작성수 초과
    public static final String ER13 = "ER13";

    // 출금 신청서 조회 오류(찾을 수 없음)
    public static final String ER14 = "ER14";

    // 주최자가 아닌 자가 주최자 권한 업무를 시도함
    public static final String ER15 = "ER15";

    // 이메일 중복 오류
    public static final String ER16 = "ER16";

    // 공동 관리자가 아닌 자가 출금 승인, 거절 시도
    public static final String ER17 = "ER17";

    // 존재하지 않는 이메일 조회 오류
    public static final String ER18 = "ER18";

    // refresh Token 불일치 오류
    public static final String ER19 = "ER19";

    // Request 데이터 타입 오류 (Json이 아님)
    public static final String ER20 = "ER20";
}
