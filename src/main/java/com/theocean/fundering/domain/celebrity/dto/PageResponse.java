package com.theocean.fundering.domain.celebrity.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final int currentPage;
    private final boolean isLast;

    public PageResponse(final Slice<T> sliceContent) {
        content = sliceContent.getContent();
        currentPage = sliceContent.getNumber() + 1;
        isLast = sliceContent.isLast();
    }
}
