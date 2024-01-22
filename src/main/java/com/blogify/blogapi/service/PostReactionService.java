package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.model.PostReaction;
import com.blogify.blogapi.repository.PostReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostReactionService {
    private final PostReactionRepository postReactionRepository;

    public List<PostReaction> findAll(){
        return postReactionRepository.findAll();
    }
    /**
    public PostReaction savePostReaction(PostReaction postReaction){
        return postReactionRepository.save(postReaction);
    }

    public PostReaction updatePostReaction(String id,PostReaction updatePostReaction){
        PostReaction postReaction = postReactionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("reaction with id " + id + " not found"));
        if(postReaction != null){
            postReaction.setReactionType(updatePostReaction.getReactionType());
            return  postReactionRepository.save(postReaction);
        }else
            return null;
    }

    public boolean isExists(String id){
        return postReactionRepository.existsById(id);
    }
    */
}
