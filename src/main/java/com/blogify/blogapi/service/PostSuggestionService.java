package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.UserCategoryPointRepository;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.repository.model.UserCategory;
import com.blogify.blogapi.repository.model.UserCategoryPoint;
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

  public List<Post> getSuggestionPost(User user, List<Post> posts, Integer page, Integer pageSase) {
    List<UserCategoryPoint> userCategoryPoints =
        userCategoryPointRepository.findByUser_Id(user.getId());
    List<Post> sortedPost =
        posts.stream()
            .sorted(
                Comparator.comparingDouble(
                    post -> {
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
                      System.out.println(
                          "-----------------////////////////----------------------------------------");
                      System.out.println(post.getId() + " point = " + note);
                      System.out.println("the user is: " + user.getId());
                      return note;
                    }))
            .toList();
    System.out.println(
        "*******************************************************************************************");
    return sortedPost.subList((page - 1) * (pageSase), page * (pageSase) - 1);
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
      Long postViewNumber = 1L;
      if (post.getPostCategories().contains(category)) {
        postViewNumber = post.getPointByView();
      }
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
        userCategoryViewNumber = userCategoryPoint.getViewNumber();
        userCategoryPostedNumber = userCategoryPoint.getViewNumber();
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
        (passesThrough0WithFiniteLimit(1_00.0, 0.001, ((double) userCategoryViewNumber))
            + passesThrough0WithFiniteLimit(1_00.0, 0.004, ((double) userCategoryPostedNumber))
            + +1.0);
    if (!isATarget) {
      calculatingUserPoint = calculatingUserPoint / 4.0;
    }

    Double calculatingPostPoint;
    if (postCategoryNumber < 5L) {
      calculatingPostPoint =
          ((double) postViewNumber)
              * Math.pow(1.2, (double) likeNumber)
              * Math.pow(0.8, (double) dislikeNumber);
    } else {
      calculatingPostPoint =
          ((double) postViewNumber)
              * Math.pow(1.2, (double) likeNumber)
              * Math.pow(0.8, (double) dislikeNumber)
              * 5.0
              / (double) postCategoryNumber;
    }
    return calculatingPostPoint * calculatingUserPoint;
  }

  private Double passesThrough0WithFiniteLimit(Double limit, Double speed, Double x) {
    return (1.0 - Math.pow((x * speed / limit) + 1.0, -0.5)) * limit;
  }
}
