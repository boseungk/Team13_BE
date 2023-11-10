package com.theocean.fundering.domain.member.repository;

import com.theocean.fundering.domain.celebrity.domain.Follow;
import com.theocean.fundering.domain.member.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Admin.PK> {
    @Query("SELECT a.postId FROM Admin a WHERE a.memberId = :userId")
    List<Long> findByUserId(@Param("userId")Long userId);

    @Query(value = "INSERT INTO admin(member_id, post_id) VALUES(:memberId, :postId)", nativeQuery = true)
    void saveCoAdmin(@Param("memberId") Long memberId, @Param("postId") Long postId);
}
