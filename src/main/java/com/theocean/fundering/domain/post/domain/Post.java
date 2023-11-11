package com.theocean.fundering.domain.post.domain;


import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import com.theocean.fundering.domain.post.service.PostEventListener;
import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@EntityListeners({AuditingEntityListener.class, PostEventListener.class})
@Table(name = "post")
@DynamicUpdate
public class Post extends AuditingFields {

    @Column(name = "heartCount")
    @Min(0)
    int heartCount;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celeb_id")
    private Celebrity celebrity;
    @Column(nullable = false, length = 100, name = "title")
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT", name = "introduction")
    private String introduction;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(nullable = false, name = "thumbnail")
    private String thumbnail;
    @Column(nullable = false, name = "targetPrice")
    @Min(1000)
    private int targetPrice;
    @Column(name = "participants")
    @Min(0)
    private int participants;
    @Column(nullable = false, name = "deadline")
    @DateTimeFormat
    private LocalDateTime deadline;
    @Enumerated(EnumType.STRING)
    @Column(name = "post_status")
    private PostStatus postStatus;


    @Builder
    public Post(final Long postId, final Member writer, final Celebrity celebrity, final String title, final String introduction, final String thumbnail, final int targetPrice, final int participants, final LocalDateTime deadline, final PostStatus postStatus) {
        this.postId = postId;
        this.writer = writer;
        this.celebrity = celebrity;
        this.title = title;
        this.introduction = introduction;
        this.thumbnail = thumbnail;
        this.targetPrice = targetPrice;
        this.participants = participants;
        this.deadline = deadline;
        this.postStatus = postStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Post post)) return false;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    public void update(final String title, final String introduction, final String thumbnail, final int targetPrice, final LocalDateTime deadline, final LocalDateTime modifiedAt) {
        this.title = title;
        this.introduction = introduction;
        this.thumbnail = thumbnail;
        this.targetPrice = targetPrice;
        this.deadline = deadline;
        this.modifiedAt = modifiedAt;
    }

    public void registerAccount(final Account account) {
        this.account = account;
    }

    public void addHeartCount() {
        heartCount += 1;
    }

    public void subtractHeartCount() {
        heartCount -= 1;
    }

    public void changeStatus(final PostStatus postStatus) {
        this.postStatus = postStatus;
    }

}