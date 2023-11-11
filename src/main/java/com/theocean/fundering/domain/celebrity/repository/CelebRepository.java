package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CelebRepository extends JpaRepository<Celebrity, Long>, CelebRepositoryCustom {

    Optional<Celebrity> findByCelebId(Long celebId);
    
    @Query(value = "SELECT * FROM celebrity c WHERE approval_status = 'APPROVED' ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Celebrity> findAllRandom();

    @Query(value = "SELECT rank FROM (SELECT id, RANK() OVER (ORDER BY follower_count DESC) as rank FROM celebrity) AS ranked_celebs WHERE id = :celebId", nativeQuery = true)
    Integer getFollowerRank(@Param("celebId") Long celebId);

}
