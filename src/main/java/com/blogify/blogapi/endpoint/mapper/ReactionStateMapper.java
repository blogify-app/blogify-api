package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReactionStateMapper {
    public ReactionStat toRest(com.blogify.blogapi.repository.model.ReactionState domain) {
        return new ReactionStat()
                .likes(domain.getLikes())
                .dislikes(domain.getDislikes());
    }
    public List<ReactionStat> toRestList(List<com.blogify.blogapi.repository.model.ReactionState> domains) {
        return domains.stream()
                .map(this::toRest)
                .collect(Collectors.toList());
    }
}
