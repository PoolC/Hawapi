package com.moviePicker.api.reviewReportMember.repository;

import com.moviePicker.api.reviewReportMember.domain.ReviewReportMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportMemberRepository extends JpaRepository<ReviewReportMember,Long> {
}
