package com.theocean.fundering.domain.member.repository;

import com.theocean.fundering.domain.celebrity.domain.Follow;
import com.theocean.fundering.domain.member.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Admin.PK> {
    @Query("SELECT a.postId FROM Admin a WHERE a.memberId = :userId")
    Long findByUserId(@Param("userId")Long userId);
}
