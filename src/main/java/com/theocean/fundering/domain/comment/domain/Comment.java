package com.theocean.fundering.domain.comment.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comment", indexes = @Index(name = "index_comment_order", columnList = "comment_order", unique = false))
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long commentId;

    @Column(nullable = false, name = "writer_id")
    private Long writerId;

    @Column(nullable = false, name = "post_id")
    private Long postId;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Column(nullable = false, name = "comment_order")
    private String commentOrder;

    @Builder
    public Comment(final Long writerId, final Long postId, final String content) {
        this.writerId = writerId;
        this.postId = postId;
        this.content = content;
        isDeleted = false;
    }

    public void updateCommentOrder(final String commentOrder) {
        this.commentOrder = commentOrder;
    }

    public long getEpochSecond() {
        return createdAt.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public int getDepth() {
        return (int) commentOrder.chars().filter(ch -> '.' == ch).count();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Comment comment)) return false;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
