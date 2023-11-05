package com.theocean.fundering.domain.post.domain.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum PostStatus {
    ONGOING("ONGOING"),
    COMPLETE("COMPLETE"),
    CLOSED("CLOSED");

    private final String type;

    PostStatus(final String type) {
        this.type = type;
    }

    public static PostStatus fromString(final String type) {
        return Arrays.stream(values())
                .filter(postStatus -> postStatus.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public String getType() {
        return type;
    }

    public class PostStatusToStringConverter implements AttributeConverter<PostStatus, String> {

        @Override
        public String convertToDatabaseColumn(final PostStatus attribute) {
            return attribute.getType();
        }

        @Override
        public PostStatus convertToEntityAttribute(final String dbData) {
            return fromString(dbData);
        }
    }
}
