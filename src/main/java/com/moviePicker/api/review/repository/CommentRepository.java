package com.moviePicker.api.review.repository;

import com.moviePicker.api.review.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
