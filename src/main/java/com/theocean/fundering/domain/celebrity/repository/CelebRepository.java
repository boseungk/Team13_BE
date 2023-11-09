package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CelebRepository extends JpaRepository<Celebrity, Long>, CelebRepositoryCustom {

    @Query(value = "SELECT * FROM celebrity c WHERE approval_status = 'APPROVED' ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Celebrity> findAllRandom();


    @Query(value = "SELECT rank FROM " +
            "(SELECT id, RANK() OVER (ORDER BY follower_count DESC) as rank FROM celebrity )" +
            "WHERE id = :celebId", nativeQuery = true)
    int getFollowerRank(@Param("celebId") Long celebId);

}
