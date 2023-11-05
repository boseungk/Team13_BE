package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {
    @Query("SELECT COUNT(*) FROM Follow f WHERE f.celebId = :celebId")
    int countByCelebId(@Param("celebId") Long celebId);

    @Query("SELECT COUNT(*) FROM Follow f WHERE f.celebId = :celebId AND f.memberId = :followId")
    int countByCelebIdAndFollowId(@Param("celebId") Long celebId, @Param("followId") Long followId);

    @Query("SELECT f.celebId FROM Follow f WHERE f.memberId = :memberId ")
    List<Long> findAllFollowingCelebById(@Param("memberId") Long memberId);

    @Modifying
    @Query(value = "INSERT INTO follow(celebrity_id, member_id) VALUES(:celebId, :memberId)", nativeQuery = true)
    void saveFollow(@Param("celebId") Long celebId, @Param("memberId") Long memberId);

    @Modifying
    @Query(value = "DELETE FROM follow WHERE celebrity_id = :celebId AND member_id = :memberId", nativeQuery = true)
    void saveUnFollow(@Param("celebId") Long celebId, @Param("memberId") Long memberId);
}
