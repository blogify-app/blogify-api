package com.blogify.blogapi.service.event;

import com.blogify.blogapi.model.PostReaction;
import com.blogify.blogapi.repository.PostReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class PostReactionService {
    private PostReactionRepository postReactionRepository;
}
