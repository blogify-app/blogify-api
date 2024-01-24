package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.PostMapper;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.repository.model.PostReaction;
import com.blogify.blogapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping("/posts")
    public List<com.blogify.blogapi.endpoint.rest.model.Post> findAllPost(){
        return postService.findAll().stream().map(postMapper::toRest).toList();
    }
    @PutMapping("/posts/{postId}")
    public com.blogify.blogapi.endpoint.rest.model.Post putPost(@PathVariable String postId, @RequestBody Post post){
        if(postService.isExists(postId)){
            return postMapper.toRest(postService.updatePost(postId,postMapper.toDomain(post)));
        }else
            return postMapper.toRest(postService.savePost(postMapper.toDomain(post)));
    }
    @GetMapping("/posts/{postId}/reactions")
    public List<PostReaction> getPostReaction(@PathVariable String postId){
        return postService.findById(postId).getPostReactions();
    }
}
