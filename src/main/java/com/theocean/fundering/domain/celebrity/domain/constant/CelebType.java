package com.theocean.fundering.domain.celebrity.domain.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum CelebType {
    SINGER("singer"),
    ACTOR("actor"),
    COMEDIAN("comedian"),
    SPORT("sport"),
    INFLUENCER("influencer"),
    ETC("etc");
    private final String type;

    CelebType(final String type) {
        this.type = type;
    }

    public static CelebType fromString(final String type) {
        return Arrays.stream(values())
                .filter(celebType -> celebType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public String getType() {
        return type;
    }

    public class CelebTypeToStringConverter implements AttributeConverter<CelebType, String> {

        @Override
        public String convertToDatabaseColumn(final CelebType attribute) {
            return attribute.getType();
        }

        @Override
        public CelebType convertToEntityAttribute(final String dbData) {
            return fromString(dbData);
        }
    }
}
