package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.model.Post;
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

    @GetMapping("/posts")
    public List<Post> findAllPost(){
        return postService.findAll();
    }
    @PutMapping("/posts/{postId}")
    public Post putPost(@PathVariable String postId,@RequestBody Post newPost){
        if(postService.isExists(postId)){
            return postService.updatePost(postId,newPost);
        }else
            return postService.savePost(newPost);
    }
}
