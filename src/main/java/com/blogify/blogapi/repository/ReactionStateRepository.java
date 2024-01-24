package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.ReactionState;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionStateRepository extends JpaRepository<ReactionState, BigDecimal> {
}
