package com.theocean.fundering.domain.follow.domain;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
@Entity
public class Follow {
    @Id
    @GeneratedValue
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Celebrity celebrity;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Follow follow)) return false;
        return Objects.equals(followId, follow.followId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId);
    }
}
