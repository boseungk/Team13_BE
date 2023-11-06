package com.theocean.fundering.domain.member.domain;

import com.theocean.fundering.domain.account.domain.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "admin",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "post_id"}))
@IdClass(Admin.PK.class)
@Entity
public class Admin{
    // 유저
    @Id
    @Column(name="member_id", insertable = false, updatable = false)
    private Long memberId;

    // 게시물
    @Id
    @Column(name="post_id", insertable = false, updatable = false)
    private Long postId;

    public static class PK implements Serializable {
        Long memberId;
        Long postId;
    }
}
