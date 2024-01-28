package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.PostPicture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPictureRepository extends JpaRepository<PostPicture, String> {
  List<PostPicture> findAllByPostId(String postId);
  PostPicture findByIdAndPostId(String id, String postId);
}
