package com.moviePicker.api.member.repository;

import com.moviePicker.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    public void deleteByNickname(String nickname);
}
