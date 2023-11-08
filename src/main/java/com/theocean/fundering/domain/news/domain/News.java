package com.theocean.fundering.domain.news.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "news")
public class News extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long newsId;

    @Column(nullable = false, name = "post_id")
    private Long postId;

    @Column(nullable = false, name = "writer_id")
    private Long writerId;

    @Column(nullable = false, length = 100, name = "title")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT", name = "content")
    private String content;


    @Builder
    public News(final Long postId, final Long writerId, final String title, final String content) {
        this.postId = postId;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final News news)) return false;
        return Objects.equals(newsId, news.newsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId);
    }
}
