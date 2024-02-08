package com.blogify.blogapi.repository.dao;

import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostCategory;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class PostDao {
  private EntityManager entityManager;

  public List<Post> findByCriteria(String categories, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> query = builder.createQuery(Post.class);
    Root<Post> root = query.from(Post.class);
    Join<Post, PostCategory> postCategoryJoin = root.join("postCategories", JoinType.LEFT);
    Join<PostCategory, Category> categoryJoin = postCategoryJoin.join("category", JoinType.LEFT);

    Predicate predicate = builder.conjunction();

    if (categories != null && !categories.isEmpty()) {
      List<String> categoreisList = Arrays.stream(categories.split(",")).toList();
      predicate =
          builder.equal(
              builder.lower(categoryJoin.get("name")), categoreisList.get(0).toLowerCase());
      for (String category : categoreisList) {
        predicate =
            builder.or(
                predicate,
                builder.or(
                    builder.equal(
                        builder.lower(categoryJoin.get("name")), category.toLowerCase())));
      }
    }

    query
        .distinct(true)
        .where(predicate)
        .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder))
        .orderBy(builder.desc(root.get("creationDatetime")));

    return entityManager
        .createQuery(query)
        .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize())
        .setMaxResults(pageable.getPageSize())
        .getResultList();
  }
}
