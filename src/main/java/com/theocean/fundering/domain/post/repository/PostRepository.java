package com.theocean.fundering.domain.post.repository;


import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.constant.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long>, PostQuerydslRepository {
    @Query("SELECT p FROM Post p WHERE p.celebrity.celebId = :celebId")
    List<Post> findPostByCelebId(@Param("celebId") Long celebId);

    @Query("SELECT COUNT(*) FROM Post p WHERE p.celebrity.celebId = :celebId and p.postStatus = :postStatus")
    int countByPostStatus(@Param("celebId") Long celebId, @Param("postStatus") PostStatus postStatus);

}