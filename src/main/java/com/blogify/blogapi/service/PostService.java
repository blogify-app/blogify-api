package com.blogify.blogapi.service;

import com.blogify.blogapi.model.Post;
import com.blogify.blogapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService{
    private final PostRepository postRepository;

    public List<Post> getAll(){
        return postRepository.findAll();
    }
    public Post getById(String id){
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }
}
