package com.blogify.blogapi.repository.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"reaction_state\"")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReactionState extends Reaction{
    private BigDecimal likes;
    private BigDecimal dislikes;
}
