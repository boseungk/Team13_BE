package com.theocean.fundering.domain.post.repository;


import com.theocean.fundering.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long>, PostQuerydslRepository {
    @Query("SELECT p FROM Post p WHERE p.celebrity.celebId = :celebId")
    List<Post> findPostByCelebId(@Param("celebId") Long celebId);
}