package com.theocean.fundering.domain.heart.repository;

import com.theocean.fundering.domain.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Modifying
    @Query(value = "INSERT INTO heart(member_id, post_id) VALUES(:memberId, :postId)", nativeQuery = true)
    void saveHeart(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Modifying
    @Query(value = "DELETE FROM heart WHERE member_id = :memberId AND post_id = :postId", nativeQuery = true)
    void saveUnHeart(@Param("memberId") Long memberId, @Param("postId") Long postId);
}

