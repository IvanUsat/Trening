package com.jm.online_store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "parent_comment")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "comment_date")
    @CreationTimestamp
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @Column(name = "product_id")
    private Long productId;
}
