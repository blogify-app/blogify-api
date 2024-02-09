package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.UserCategoryPointRepository;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostCategory;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.repository.model.UserCategory;
import com.blogify.blogapi.repository.model.UserCategoryPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostSuggestionService {
  private final CategoryService categoryService;
  private final UserCategoryPointRepository userCategoryPointRepository;
  private final PostReactionService postReactionService;

  public List<Post> getSuggestionPost(
      User user, List<Post> posts, Integer page, Integer pageSase, String categories) {
    List<UserCategoryPoint> userCategoryPoints =
        userCategoryPointRepository.findByUser_Id(user.getId());
    List<Post> filterPost = posts;

    if (categories != null && !categories.isEmpty()) {
      List<String> categoreisList = Arrays.stream(categories.split(",")).toList();
      filterPost =
          posts.stream()
              .filter(
                  post -> {
                    for (String categoryName : categoreisList) {
                      if (post.getPostCategories().stream()
                          .map(postCategory -> postCategory.getCategory().getName())
                          .anyMatch(string -> string.equals(categoryName))) {
                        return true;
                      }
                    }
                    return false;
                  })
              .toList();
    }
    List<Post> filterPostList = new ArrayList<>();
    if (filterPost != null) {

      filterPostList = new ArrayList<>(filterPost);
      filterPostList.sort(
          Comparator.comparingDouble(
                  postToCheck -> {
                    Post post = (Post) postToCheck;
                    Double note =
                        getPostSuggestionPoint(
                            post,
                            userCategoryPoints,
                            postReactionService
                                .getReactionStat(post.getId())
                                .getLikes()
                                .longValue(),
                            postReactionService
                                .getReactionStat(post.getId())
                                .getDislikes()
                                .longValue(),
                            user.getUserCategories().stream()
                                .map(UserCategory::getCategory)
                                .toList(),
                            (long) post.getPostCategories().size());
                    return note;
                  })
              .reversed());
    }

    return filterPostList.subList(
        Math.min(filterPostList.size(), (page - 1) * pageSase),
        Math.min(filterPostList.size(), page * pageSase));
  }

  public Double getPostSuggestionPoint(
      Post post,
      List<UserCategoryPoint> userCategoryPoints,
      Long likeNumber,
      Long dislikeNumber,
      List<Category> userCategoryTargets,
      Long postCategoryNumber) {
    List<Category> categories = categoryService.findAll();
    Double point = 0.0;
    for (Category category : categories) {
      // ---------------- USER -----------------

      Long postViewNumber = 1L;
      if (post.getPostCategories().stream()
          .map(PostCategory::getCategory)
          .toList()
          .contains(category)) {
        postViewNumber = Math.max(1L, post.getPointByView());
      }

      // ---------------- POST -----------------
      Boolean isATarget = userCategoryTargets.contains(category);
      Long userCategoryViewNumber = 1L;
      Long userCategoryPostedNumber = 1L;
      if (userCategoryPoints.stream()
          .map(UserCategoryPoint::getCategory)
          .toList()
          .contains(category)) {
        UserCategoryPoint userCategoryPoint =
            userCategoryPoints.stream()
                .filter(userCategoryPoint1 -> userCategoryPoint1.getCategory().equals(category))
                .findFirst()
                .get();
        userCategoryViewNumber = Math.max(1L, userCategoryPoint.getViewNumber());
        userCategoryPostedNumber = Math.max(1L, userCategoryPoint.getPostedNumber());
      }
      point +=
          getCategorySuggestionPoint(
              postViewNumber,
              userCategoryViewNumber,
              userCategoryPostedNumber,
              likeNumber,
              dislikeNumber,
              isATarget,
              postCategoryNumber);
    }
    return point;
  }

  public Double getCategorySuggestionPoint(
      Long postViewNumber,
      Long userCategoryViewNumber,
      Long userCategoryPostedNumber,
      Long likeNumber,
      Long dislikeNumber,
      Boolean isATarget,
      Long postCategoryNumber) {
    Double calculatingUserPoint =
        userCategoryViewNumberFunction((double) userCategoryViewNumber)
            + userCategoryPostedNumberFunction((double) userCategoryPostedNumber)
            + 1.0;
    if (!isATarget) {
      calculatingUserPoint = calculatingUserPoint / 4.0;
    }

    Double calculatingPostPoint =
        ((double) postViewNumber)
            * likeAndDislikeCoefficient(likeNumber, dislikeNumber)
            * postCategoryNumberCoefficient(postCategoryNumber);

    return calculatingPostPoint * calculatingUserPoint;
  }

  private Double passesThrough0WithFiniteLimit(Double limit, Double speed, Double x) {
    return (1.0 - Math.pow((x * speed / limit) + 1.0, -0.5)) * limit;
  }

  private Double userCategoryViewNumberFunction(Double x) {
    return passesThrough0WithFiniteLimit(1_00.0, 0.001, x);
  }

  private Double userCategoryPostedNumberFunction(Double x) {
    return passesThrough0WithFiniteLimit(1_00.0, 0.04, x);
  }

  private Double likeAndDislikeCoefficient(Long likeNumber, Long dislikeNumber) {
    return Math.pow(1.1, (double) likeNumber) * Math.pow(0.9, (double) dislikeNumber);
  }

  private Double postCategoryNumberCoefficient(Long postCategoryNumber) {
    return postCategoryNumber < 5L ? 1.0 : (5.0 / (double) postCategoryNumber);
  }
}
