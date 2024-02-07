package com.blogify.blogapi.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReactionStat {
  private BigDecimal dislikes = BigDecimal.ZERO;
  private BigDecimal likes = BigDecimal.ZERO;
}
