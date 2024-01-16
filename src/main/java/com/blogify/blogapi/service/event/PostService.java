package com.blogify.blogapi.service.event;

import com.blogify.blogapi.model.Post;
import com.blogify.blogapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService{
    private final PostRepository postRepository;

    public List<Post> getAll(){
        return postRepository.findAll();
    }
    public Optional<Post> getById(String id){
        return postRepository.findById(id);
    }
}
