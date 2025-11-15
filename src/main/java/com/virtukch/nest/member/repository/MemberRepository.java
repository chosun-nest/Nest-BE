package com.virtukch.nest.member.repository;

import com.virtukch.nest.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberEmail(String username);

    // 이름으로 회원 검색 (동명이인 가능)
    List<Member> findByMemberNameContaining(String memberName);
}
