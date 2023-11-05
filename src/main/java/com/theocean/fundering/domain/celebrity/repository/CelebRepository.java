package com.theocean.fundering.domain.celebrity.repository;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CelebRepository extends JpaRepository<Celebrity, Long>, CelebRepositoryCustom {
    @Query(value = "SELECT * FROM celebrity order by RAND() limit 3", nativeQuery = true)
    List<Celebrity> findAllRandom();

    @Query(value = "SELECT rank FROM " +
            "(SELECT celebrity_id, RANK() OVER (ORDER BY follower_count DESC) as rank FROM celebrity )" +
            "WHERE celebrity_id = :celebId", nativeQuery = true)
    int getFollowerRank(@Param("celebId") Long celebId);

}
