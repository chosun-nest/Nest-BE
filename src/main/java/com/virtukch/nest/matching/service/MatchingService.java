package com.virtukch.nest.matching.service;

import com.virtukch.nest.follow.repository.FollowRepository;
import com.virtukch.nest.matching.dto.MatchingMemberResponseDto;
import com.virtukch.nest.matching.dto.MatchingSearchResponseDto;
import com.virtukch.nest.matching.dto.PopularMembersResponseDto;
import com.virtukch.nest.matching.repository.MatchingRepository;
import com.virtukch.nest.member.model.Member;
import com.virtukch.nest.member_department.service.MemberDepartmentService;
import com.virtukch.nest.member_interest.service.MemberInterestService;
import com.virtukch.nest.member_tech_stack.service.MemberTechStackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final FollowRepository followRepository;
    private final MemberDepartmentService memberDepartmentService;
    private final MemberInterestService memberInterestService;
    private final MemberTechStackService memberTechStackService;

    /**
     * 1. 사용자 검색 (이름, 전공, 학번)
     */
    public MatchingSearchResponseDto searchMembers(Long currentUserId, String keyword, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage;

        switch (type.toLowerCase()) {
            case "name":
                memberPage = matchingRepository.searchByName(keyword, pageable);
                break;
            case "major":
                memberPage = matchingRepository.searchByMajor(keyword, pageable);
                break;
            case "studentid":
                memberPage = matchingRepository.searchByStudentId(keyword, pageable);
                break;
            default:
                throw new IllegalArgumentException("Invalid search type: " + type);
        }

        List<MatchingMemberResponseDto> members = memberPage.getContent().stream()
                .filter(member -> !member.getMemberId().equals(currentUserId))
                .map(member -> convertToMatchingMemberDto(member, currentUserId, false, false))
                .collect(Collectors.toList());

        return MatchingSearchResponseDto.builder()
                .members(members)
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .build();
    }

    /**
     * 2. 관심사 기반 매칭
     */
    public MatchingSearchResponseDto findByCommonInterests(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = matchingRepository.findByCommonInterests(currentUserId, pageable);

        List<MatchingMemberResponseDto> members = memberPage.getContent().stream()
                .map(member -> convertToMatchingMemberDto(member, currentUserId, true, false))
                .collect(Collectors.toList());

        return MatchingSearchResponseDto.builder()
                .members(members)
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .build();
    }

    /**
     * 3. 같은 학과 + 관심사 기반 매칭
     */
    public MatchingSearchResponseDto findBySameMajorAndCommonInterests(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = matchingRepository.findBySameMajorAndCommonInterests(currentUserId, pageable);

        List<MatchingMemberResponseDto> members = memberPage.getContent().stream()
                .map(member -> convertToMatchingMemberDto(member, currentUserId, true, false))
                .collect(Collectors.toList());

        return MatchingSearchResponseDto.builder()
                .members(members)
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .build();
    }

    /**
     * 4. 인기 사용자 추천
     */
    public PopularMembersResponseDto findPopularMembers(Long currentUserId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Member> members = matchingRepository.findPopularMembers(currentUserId, pageable);

        List<MatchingMemberResponseDto> popularMembers = members.stream()
                .map(member -> convertToMatchingMemberDto(member, currentUserId, false, false))
                .collect(Collectors.toList());

        return PopularMembersResponseDto.builder()
                .members(popularMembers)
                .build();
    }

    /**
     * 5. 기술 스택 기반 매칭
     */
    public MatchingSearchResponseDto findByCommonTechStacks(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = matchingRepository.findByCommonTechStacks(currentUserId, pageable);

        List<MatchingMemberResponseDto> members = memberPage.getContent().stream()
                .map(member -> convertToMatchingMemberDto(member, currentUserId, false, true))
                .collect(Collectors.toList());

        return MatchingSearchResponseDto.builder()
                .members(members)
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .build();
    }

    /**
     * 6. 신규 회원 조회
     */
    public MatchingSearchResponseDto findNewMembers(Long currentUserId, int days, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // N일 이전 날짜 계산
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        Page<Member> memberPage = matchingRepository.findNewMembers(startDate, currentUserId, pageable);

        List<MatchingMemberResponseDto> members = memberPage.getContent().stream()
                .map(member -> {
                    MatchingMemberResponseDto dto = convertToMatchingMemberDto(member, currentUserId, false, false);
                    // 가입일 정보 추가
                    return MatchingMemberResponseDto.builder()
                            .memberId(dto.getMemberId())
                            .memberName(dto.getMemberName())
                            .memberEmail(dto.getMemberEmail())
                            .memberImageUrl(dto.getMemberImageUrl())
                            .memberIntroduce(dto.getMemberIntroduce())
                            .memberDepartmentResponseDtoList(dto.getMemberDepartmentResponseDtoList())
                            .memberInterestResponseDtoList(dto.getMemberInterestResponseDtoList())
                            .memberTechStackResponseDtoList(dto.getMemberTechStackResponseDtoList())
                            .followerCount(dto.getFollowerCount())
                            .joinedDate(member.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return MatchingSearchResponseDto.builder()
                .members(members)
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .build();
    }

    /**
     * Member를 MatchingMemberResponseDto로 변환
     */
    private MatchingMemberResponseDto convertToMatchingMemberDto(
            Member member,
            Long currentUserId,
            boolean includeInterestMatch,
            boolean includeTechStackMatch) {

        Long memberId = member.getMemberId();

        // 팔로워 수 조회
        Long followerCount = matchingRepository.countFollowers(memberId);

        // 관심사 매칭 정보
        Integer matchCount = null;
        List<String> commonInterests = null;
        if (includeInterestMatch) {
            matchCount = matchingRepository.countCommonInterests(memberId, currentUserId);
            commonInterests = matchingRepository.findCommonInterestNames(memberId, currentUserId);
        }

        // 기술 스택 매칭 정보
        Integer stackMatchCount = null;
        List<String> commonStacks = null;
        if (includeTechStackMatch) {
            stackMatchCount = matchingRepository.countCommonTechStacks(memberId, currentUserId);
            commonStacks = matchingRepository.findCommonTechStackNames(memberId, currentUserId);
        }

        return MatchingMemberResponseDto.builder()
                .memberId(memberId)
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberImageUrl(member.getMemberImageUrl() != null ?
                        member.getMemberImageUrl() : "/uploaded-images/default/default.png")
                .memberIntroduce(member.getMemberIntroduce())
                .memberDepartmentResponseDtoList(memberDepartmentService.findByMemberId(memberId))
                .memberInterestResponseDtoList(memberInterestService.findByMemberId(memberId))
                .memberTechStackResponseDtoList(memberTechStackService.findByMemberId(memberId))
                .followerCount(followerCount)
                .matchCount(matchCount)
                .commonInterests(commonInterests)
                .stackMatchCount(stackMatchCount)
                .commonStacks(commonStacks)
                .build();
    }
}