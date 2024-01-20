package com.blogify.blogapi.service;

import com.blogify.blogapi.model.Post;
import com.blogify.blogapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService{
    private final PostRepository postRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findById(String id){
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }

    public Post savePost(Post post){
        post.setCreationDatetime(Instant.now());
        post.setLastUpdateDatetime(Instant.now());
        return postRepository.save(post);
    }

    public Post updatePost(String id, Post updatePost){
        Post post = findById(id);
        if(post != null){
            post.setTitle(updatePost.getTitle());
            post.setContent(updatePost.getContent());
            post.setLastUpdateDatetime(Instant.now());
            return postRepository.save(post);
        }else
            return null;
    }

    public boolean isExists(String id){
        return postRepository.existsById(id);
    }
}
