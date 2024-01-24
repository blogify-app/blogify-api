package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,String> {
    @Query(
            "SELECT p FROM Post p "
                    + "JOIN p.postCategories pc "
                    + "JOIN pc.category c "
                    + "WHERE c.name LIKE %:categoryName%")
    List<Post> getPostsByCategory(@Param(value = "categoryName") String categoryName, Pageable pageable);
}
