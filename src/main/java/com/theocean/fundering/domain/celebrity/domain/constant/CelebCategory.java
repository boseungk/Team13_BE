package com.theocean.fundering.domain.celebrity.domain.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum CelebCategory {
    SINGER("singer"),
    ACTOR("actor"),
    COMEDIAN("comedian"),
    SPORT("sport"),
    INFLUENCER("influencer"),
    ETC("etc");
    private final String type;

    CelebCategory(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CelebCategory fromString(final String type){
        return Arrays.stream(values())
                .filter(celebType -> celebType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public class CelebCategoryToStringConverter implements AttributeConverter<CelebCategory, String>{

        @Override
        public String convertToDatabaseColumn(CelebCategory attribute) {
            return attribute.getType();
        }

        @Override
        public CelebCategory convertToEntityAttribute(String dbData) {
            return fromString(dbData);
        }
    }
}
