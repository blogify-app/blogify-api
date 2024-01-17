package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.PostReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostReactionService {
    private final PostReactionRepository postReactionRepository;
}
