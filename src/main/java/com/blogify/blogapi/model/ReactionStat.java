package com.blogify.blogapi.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionStat {
  private BigDecimal dislikes = BigDecimal.ZERO;
  private BigDecimal likes = BigDecimal.ZERO;
}
