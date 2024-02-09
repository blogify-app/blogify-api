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
    System.out.println("userCategoryPoints = " + userCategoryPoints);
    List<Post> filterPost = posts;

    for (Post p : filterPost) {
      System.out.println(
          "--- --- ---- ----Post wit id : "
              + p.getId()
              + " have value = "
              + getPostSuggestionPoint(
                  p,
                  userCategoryPoints,
                  postReactionService.getReactionStat(p.getId()).getLikes().longValue(),
                  postReactionService.getReactionStat(p.getId()).getDislikes().longValue(),
                  user.getUserCategories().stream().map(UserCategory::getCategory).toList(),
                  (long) p.getPostCategories().size()));
    }

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
      System.out.println(filterPost);
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
                    System.out.println("The cathegory is : " + categoryService);
                    System.out.println(post.getId() + " point = " + note);
                    System.out.println("the user is: " + user.getId());
                    System.out.println(
                        "-----------------////////////////----------------------------------------");
                    return note;
                  })
              .reversed());
    }

    System.out.println(
        "*******************************************************************************************");
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
      System.out.println("| fro catogory : " + category.getName() + "**********");

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
    System.out.println("_");
    System.out.println("---delail---");
    System.out.println("postViewNumber = " + postViewNumber);
    System.out.println("userCategoryViewNumber = " + userCategoryViewNumber);
    System.out.println("userCategoryPostedNumber = " + userCategoryPostedNumber);
    System.out.println("likeNumber = " + likeNumber);
    System.out.println("dislikeNumber = " + dislikeNumber);
    System.out.println("isATarget = " + isATarget);
    System.out.println("postCategoryNumber = " + postCategoryNumber);
    System.out.println("---fnish delail---");
    System.out.println("_");
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
              / ((double) postCategoryNumber * 5.0);
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
