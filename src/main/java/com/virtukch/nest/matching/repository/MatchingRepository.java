package com.virtukch.nest.matching.repository;

import com.virtukch.nest.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchingRepository extends JpaRepository<Member, Long> {

    // 1. 사용자 검색 - 이름으로 검색
    @Query("SELECT m FROM Member m WHERE m.memberName LIKE %:keyword%")
    Page<Member> searchByName(@Param("keyword") String keyword, Pageable pageable);

    // 2. 사용자 검색 - 전공으로 검색
    @Query("SELECT DISTINCT m FROM Member m " +
           "JOIN MemberDepartment md ON m.memberId = md.memberId " +
           "JOIN Department d ON md.departmentId = d.departmentId " +
           "WHERE d.departmentName LIKE %:keyword%")
    Page<Member> searchByMajor(@Param("keyword") String keyword, Pageable pageable);

    // 3. 사용자 검색 - 학번으로 검색 (학번 필드가 있다면)
    // Member 엔티티에 studentId 필드가 없으므로, 이메일로 검색하도록 변경
    @Query("SELECT m FROM Member m WHERE m.memberEmail LIKE %:keyword%")
    Page<Member> searchByStudentId(@Param("keyword") String keyword, Pageable pageable);

    // 4. 관심사 기반 매칭 - 현재 사용자의 관심사와 겹치는 사용자 조회
    @Query("SELECT DISTINCT m FROM Member m " +
           "JOIN MemberInterest mi ON m.memberId = mi.memberId " +
           "WHERE mi.interestId IN (" +
           "    SELECT mi2.interestId FROM MemberInterest mi2 WHERE mi2.memberId = :currentUserId" +
           ") " +
           "AND m.memberId != :currentUserId")
    Page<Member> findByCommonInterests(@Param("currentUserId") Long currentUserId, Pageable pageable);

    // 5. 같은 학과 + 관심사 기반 매칭
    @Query("SELECT DISTINCT m FROM Member m " +
           "JOIN MemberDepartment md ON m.memberId = md.memberId " +
           "JOIN MemberInterest mi ON m.memberId = mi.memberId " +
           "WHERE md.departmentId IN (" +
           "    SELECT md2.departmentId FROM MemberDepartment md2 WHERE md2.memberId = :currentUserId" +
           ") " +
           "AND mi.interestId IN (" +
           "    SELECT mi2.interestId FROM MemberInterest mi2 WHERE mi2.memberId = :currentUserId" +
           ") " +
           "AND m.memberId != :currentUserId")
    Page<Member> findBySameMajorAndCommonInterests(@Param("currentUserId") Long currentUserId, Pageable pageable);

    // 6. 기술 스택 기반 매칭
    @Query("SELECT DISTINCT m FROM Member m " +
           "JOIN MemberTechStack mts ON m.memberId = mts.memberId " +
           "WHERE mts.techStackId IN (" +
           "    SELECT mts2.techStackId FROM MemberTechStack mts2 WHERE mts2.memberId = :currentUserId" +
           ") " +
           "AND m.memberId != :currentUserId")
    Page<Member> findByCommonTechStacks(@Param("currentUserId") Long currentUserId, Pageable pageable);

    // 7. 인기 사용자 조회 (팔로워 수 기준)
    @Query("SELECT m FROM Member m " +
           "LEFT JOIN Follow f ON m.memberId = f.followingId " +
           "WHERE m.memberId != :currentUserId " +
           "GROUP BY m.memberId " +
           "ORDER BY COUNT(f.id) DESC")
    List<Member> findPopularMembers(@Param("currentUserId") Long currentUserId, Pageable pageable);

    // 8. 특정 사용자의 팔로워 수 조회
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingId = :memberId")
    Long countFollowers(@Param("memberId") Long memberId);

    // 9. 관심사 매칭 개수 조회
    @Query("SELECT COUNT(mi) FROM MemberInterest mi " +
           "WHERE mi.memberId = :targetMemberId " +
           "AND mi.interestId IN (" +
           "    SELECT mi2.interestId FROM MemberInterest mi2 WHERE mi2.memberId = :currentUserId" +
           ")")
    Integer countCommonInterests(@Param("targetMemberId") Long targetMemberId,
                                  @Param("currentUserId") Long currentUserId);

    // 10. 기술 스택 매칭 개수 조회
    @Query("SELECT COUNT(mts) FROM MemberTechStack mts " +
           "WHERE mts.memberId = :targetMemberId " +
           "AND mts.techStackId IN (" +
           "    SELECT mts2.techStackId FROM MemberTechStack mts2 WHERE mts2.memberId = :currentUserId" +
           ")")
    Integer countCommonTechStacks(@Param("targetMemberId") Long targetMemberId,
                                   @Param("currentUserId") Long currentUserId);

    // 11. 공통 관심사 이름 목록 조회
    @Query("SELECT i.interestName FROM Interest i " +
           "WHERE i.interestId IN (" +
           "    SELECT mi.interestId FROM MemberInterest mi WHERE mi.memberId = :targetMemberId" +
           ") " +
           "AND i.interestId IN (" +
           "    SELECT mi2.interestId FROM MemberInterest mi2 WHERE mi2.memberId = :currentUserId" +
           ")")
    List<String> findCommonInterestNames(@Param("targetMemberId") Long targetMemberId,
                                          @Param("currentUserId") Long currentUserId);

    // 12. 공통 기술 스택 이름 목록 조회
    @Query("SELECT ts.techStackName FROM TechStack ts " +
           "WHERE ts.techStackId IN (" +
           "    SELECT mts.techStackId FROM MemberTechStack mts WHERE mts.memberId = :targetMemberId" +
           ") " +
           "AND ts.techStackId IN (" +
           "    SELECT mts2.techStackId FROM MemberTechStack mts2 WHERE mts2.memberId = :currentUserId" +
           ")")
    List<String> findCommonTechStackNames(@Param("targetMemberId") Long targetMemberId,
                                           @Param("currentUserId") Long currentUserId);

    // 13. 신규 회원 조회 (N일 이내 가입자)
    @Query("SELECT m FROM Member m " +
           "WHERE m.createdAt >= :startDate " +
           "AND m.memberId != :currentUserId " +
           "ORDER BY m.createdAt DESC")
    Page<Member> findNewMembers(@Param("startDate") LocalDateTime startDate,
                                 @Param("currentUserId") Long currentUserId,
                                 Pageable pageable);
}