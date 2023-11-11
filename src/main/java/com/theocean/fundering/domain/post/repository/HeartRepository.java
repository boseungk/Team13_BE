package com.theocean.fundering.domain.post.repository;

import com.theocean.fundering.domain.post.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query("SELECT IFNULL(COUNT(*), 0) FROM Heart h WHERE h.postId = :postId")
    int countByPostId(@Param("postId") Long postId);

    @Query("SELECT IFNULL(COUNT(*), 0) FROM Heart h WHERE h.postId = :postId AND h.memberId = :memberId")
    int countByPostIdAndHeartId(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query("SELECT h.postId FROM Heart h WHERE h.memberId = :memberId")
    List<Long> findAllHeartPostById(@Param("memberId") Long memberId);

    @Modifying
    @Query(value = "INSERT INTO heart(member_id, post_id) VALUES(:memberId, :postId)", nativeQuery = true)
    void addHeart(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Modifying
    @Query(value = "DELETE FROM heart WHERE member_id = :memberId AND post_id = :postId", nativeQuery = true)
    void subtractHeart(@Param("memberId") Long memberId, @Param("postId") Long postId);
}

