package com.jm.online_store.service.impl;

import com.jm.online_store.model.Comment;
import com.jm.online_store.model.User;
import com.jm.online_store.repository.CommentRepository;
import com.jm.online_store.service.interf.CommentService;
import com.jm.online_store.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    /**
     * Fetches an arrayList of all Comments by productId
     *
     * @param productId
     * @return List<comment>
     */
    @Override
    public List<Comment> findAll(Long productId) {
        return commentRepository.findAllByProductId(productId);
    }


/**
     * Find and return List of comments from database by Customer Id
     *
     * @return List<comment>
     */
    @Override
    public List<Comment> findAllByCustomer(User user) {
        return commentRepository.findCommentsByCustomer(user);
    }

    /**
     * Method checks if Comment is a new post or reply to previous comment
     * then sets a current user as author of a comment and saves to dataBase
     *
     * @param comment
     * @return Comment
     */
    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        User loggedInUser = userService.getCurrentLoggedInUser();
        if (comment.getParentId() != null) {
            comment.setParentComment(commentRepository.findById(comment.getParentId()).get());
        }
        comment.setCustomer(userService.findById(loggedInUser.getId()).get());
        return commentRepository.save(comment);
    }

    /**
     * Find and retrieve ProductComment from database by comment Id
     *
     * @return ProductComment
     */
    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    /**
     * For init comments only
     *
     * @param comment
     */
    @Override
    @Transactional
    public void addCommentInit(Comment comment) {
        commentRepository.save(comment);
    }
}
