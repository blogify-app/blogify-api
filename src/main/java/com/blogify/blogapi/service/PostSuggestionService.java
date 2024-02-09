package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.UserCategoryPointRepository;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostCategory;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.repository.model.UserCategory;
import com.blogify.blogapi.repository.model.UserCategoryPoint;
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
    System.out.println("userCategoryPoints = " + userCategoryPoints);
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
    List<Post> sortedPost =
        filterPost.stream()
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
                      System.out.println("The cathegory is : " + categoryService);
                      System.out.println(post.getId() + " point = " + note);
                      System.out.println("the user is: " + user.getId());
                      System.out.println(
                          "-----------------////////////////----------------------------------------");
                      return note;
                    }))
            .toList();
    System.out.println(
        "*******************************************************************************************");
    return sortedPost.subList(
        Math.min(sortedPost.size(), (page - 1) * pageSase),
        Math.min(sortedPost.size(), page * pageSase));
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
      if (post.getPostCategories().stream()
          .map(PostCategory::getCategory)
          .toList()
          .contains(category)) {
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
        System.out.println("userCategoryViewNumber = " + userCategoryViewNumber);
        userCategoryPostedNumber = userCategoryPoint.getPostedNumber();
        System.out.println("userCategoryPostedNumber = " + userCategoryPostedNumber);
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
    System.out.println("calculatingPostPoint = " + calculatingPostPoint);
    System.out.println("calculatingUserPoint = " + calculatingUserPoint);

    return calculatingPostPoint * calculatingUserPoint;
  }

  private Double passesThrough0WithFiniteLimit(Double limit, Double speed, Double x) {
    return (1.0 - Math.pow((x * speed / limit) + 1.0, -0.5)) * limit;
  }

  private Double userCategoryViewNumberFunction(Double x) {
    double limit = 1_00.0;
    double speed = 0.001;
    String name = "userCategoryViewNumber";
    printFunctionWithFiniteLimitDetail(limit, speed, name);
    return passesThrough0WithFiniteLimit(limit, speed, x);
  }

  private Double userCategoryPostedNumberFunction(Double x) {
    double limit = 1_00.0;
    double speed = 0.004;
    String name = "userCategoryPostedNumber";
    printFunctionWithFiniteLimitDetail(limit, speed, name);
    return passesThrough0WithFiniteLimit(limit, speed, x);
  }

  //  private Double userCategoryViewNumberFunction(Double x) {
  //    double limit = 0.0;
  //    double speed = 0.0;
  //    String name = "userCategoryViewNumber";
  //    printFunctionWithFiniteLimitDetail(limit, speed, name);
  //    return passesThrough0WithFiniteLimit(limit, speed, x);
  //  }
  //
  //  private Double userCategoryViewNumberFunction(Double x) {
  //    double limit = 0.0;
  //    double speed = 0.0;
  //    String name = "userCategoryViewNumber";
  //    printFunctionWithFiniteLimitDetail(limit, speed, name);
  //    return passesThrough0WithFiniteLimit(limit, speed, x);
  //  }
  //
  //  private Double userCategoryViewNumberFunction(Double x) {
  //    double limit = 0.0;
  //    double speed = 0.0;
  //    String name = "userCategoryViewNumber";
  //    printFunctionWithFiniteLimitDetail(limit, speed, name);
  //    return passesThrough0WithFiniteLimit(limit, speed, x);
  //  }
  //
  //  private Double userCategoryViewNumberFunction(Double x) {
  //    double limit = 0.0;
  //    double speed = 0.0;
  //    String name = "userCategoryViewNumber";
  //    printFunctionWithFiniteLimitDetail(limit, speed, name);
  //    return passesThrough0WithFiniteLimit(limit, speed, x);
  //  }
  //
  //  private Double userCategoryViewNumberFunction(Double x) {
  //    double limit = 0.0;
  //    double speed = 0.0;
  //    String name = "userCategoryViewNumber";
  //    printFunctionWithFiniteLimitDetail(limit, speed, name);
  //    return passesThrough0WithFiniteLimit(limit, speed, x);
  //  }

  private void printFunctionWithFiniteLimitDetail(Double limit, Double speed, String name) {
    System.out.println("---------Start----------------------");
    System.out.println("//// " + name + " ////");

    for (double i = 0.0; i < limit * 4 / speed; i += limit / 100.0) {
      Double y = passesThrough0WithFiniteLimit(limit, speed, i);
      if (y >= limit * 0.05) {
        System.out.println("Value at 5% = " + y + " when x = " + i + "-");
      }
    }
    for (double i = 0.0; i < limit * 4 / speed; i += limit / 100.0) {
      Double y = passesThrough0WithFiniteLimit(limit, speed, i);
      if (y >= limit * 0.20) {
        System.out.println("Value at 20% = " + y + " when x = " + i + "++");
      }
    }
    for (double i = 0.0; i < limit * 4 / speed; i += limit / 100.0) {
      Double y = passesThrough0WithFiniteLimit(limit, speed, i);
      if (y >= limit * 0.50) {
        System.out.println("Value at 50% = " + y + " when x = " + i + "+++++");
      }
    }
    for (double i = 0.0; i < limit * 4 / speed; i += limit / 100.0) {
      Double y = passesThrough0WithFiniteLimit(limit, speed, i);
      if (y >= limit * 0.70) {
        System.out.println("Value at 70% = " + y + " when x = " + i + "+++++++");
      }
    }
    for (double i = 0.0; i < limit * 4 / speed; i += limit / 100.0) {
      Double y = passesThrough0WithFiniteLimit(limit, speed, i);
      if (y >= limit * 0.80) {
        System.out.println("Value at 80% = " + y + " when x = " + i + "++++++++");
      }
    }
    System.out.println("---------end------------------------");
  }
}
