package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.UserViewOnePost;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.repository.model.UserPostView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ViewMapper {

  private final UserMapper userMapper;
  private final PostMapper postMapper;

  public UserViewOnePost toRest(UserPostView userPostView, ReactionStat reactionStat) {
    return new UserViewOnePost()
        .id(userPostView.getId())
        .user(userMapper.toRest(userPostView.getUser()))
        .post(postMapper.toRest(userPostView.getPost(), reactionStat))
        .creationDatetime(userPostView.getCreationDatetime());
  }
}
