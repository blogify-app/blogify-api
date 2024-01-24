package com.blogify.blogapi.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"post_category\"")
public class PostCategory implements Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
