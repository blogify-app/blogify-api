package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.model.PostReaction;
import com.blogify.blogapi.service.PostReactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PostReactionController {

    private final PostReactionService postReactionService;
    @GetMapping("/reactions")
    public List<PostReaction> findAllReaction(){
        return postReactionService.findAll();
    }

    @PutMapping("/reactions/{reactionId}")
    public PostReaction updateReaction(@PathVariable String reactionId , @RequestBody PostReaction postReaction){
        if(postReactionService.isExists(reactionId)){
            return postReactionService.updatePostReaction(reactionId,postReaction);
        }else
            return postReactionService.savePostReaction(postReaction);
    }
}
