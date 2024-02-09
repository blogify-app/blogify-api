package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.UserCategoryPointRepository;
import com.blogify.blogapi.repository.UserPostViewRepository;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostCategory;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.repository.model.UserCategoryPoint;
import com.blogify.blogapi.repository.model.UserPostView;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ViewService {
  private final UserService userService;
  private final UserPostViewRepository userPostViewRepository;
  private final UserCategoryPointRepository userCategoryPointRepository;

  @Transactional
  public UserPostView userViewPost(String userId, Post post) {
    User user = userService.findById(userId);
    UserPostView userPostView = UserPostView.builder().post(post).user(user).build();
    for (Category category :
        post.getPostCategories().stream().map(PostCategory::getCategory).toList()) {
      Optional<UserCategoryPoint> userCategoryPointOptional =
          userCategoryPointRepository.findByCategory_IdAndUser_Id(category.getId(), userId);
      if (userCategoryPointOptional.isPresent()) {
        UserCategoryPoint userCategoryPoint = userCategoryPointOptional.get();
        userCategoryPoint.setViewNumber(userCategoryPoint.getViewNumber() + 1L);
        userCategoryPointRepository.save(userCategoryPoint);
      } else {
        UserCategoryPoint userCategoryPoint =
            UserCategoryPoint.builder()
                .viewNumber(2L)
                .postedNumber(1L)
                .category(category)
                .user(user)
                .build();
        userCategoryPointRepository.save(userCategoryPoint);
      }
    }
    UserPostView value = userPostViewRepository.save(userPostView);
    // todo: delete after scheduler
    updateAllPostPoint();
    return value;
  }

  @Transactional
  public void updateUserCreatedPostPoint(Post post) {
    for (Category category :
        post.getPostCategories().stream().map(PostCategory::getCategory).toList()) {
      Optional<UserCategoryPoint> userCategoryPointOptional =
          userCategoryPointRepository.findByCategory_IdAndUser_Id(
              category.getId(), post.getUser().getId());
      if (userCategoryPointOptional.isPresent()) {
        UserCategoryPoint userCategoryPoint = userCategoryPointOptional.get();
        userCategoryPoint.setPostedNumber(userCategoryPoint.getPostedNumber() + 1L);
        userCategoryPointRepository.save(userCategoryPoint);
        continue;
      }
      UserCategoryPoint userCategoryPoint =
          UserCategoryPoint.builder()
              .viewNumber(1L)
              .postedNumber(2L)
              .category(category)
              .user(post.getUser())
              .build();
      userCategoryPointRepository.save(userCategoryPoint);
    }
  }

  @Transactional
  public void updatePostPoint(UserPostView userPostView) {
    Post post = userPostView.getPost();
    post.setPointByView(post.getPointByView() + 1L);
    userPostView.setUpload(true);
    userPostViewRepository.save(userPostView);
  }

  @Transactional
  public void updatePostPoint(List<UserPostView> userPostViews) {
    for (UserPostView userPostView : userPostViews) {
      updatePostPoint(userPostView);
    }
  }

  // todo: use in scheduler
  @Transactional
  public void updateAllPostPoint() {
    List<UserPostView> userPostViews = userPostViewRepository.findNotUpload();
    updatePostPoint(userPostViews);
  }
}
