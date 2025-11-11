package com.virtukch.nest.matching.dto;

import com.virtukch.nest.member_department.dto.MemberDepartmentResponseDto;
import com.virtukch.nest.member_interest.dto.MemberInterestResponseDto;
import com.virtukch.nest.member_tech_stack.dto.MemberTechStackResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class
MatchingMemberResponseDto {

    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String memberImageUrl;
    private String memberIntroduce;
    private List<MemberDepartmentResponseDto> memberDepartmentResponseDtoList;
    private List<MemberInterestResponseDto> memberInterestResponseDtoList;
    private List<MemberTechStackResponseDto> memberTechStackResponseDtoList;
    private Long followerCount;

    // 관심사 기반 매칭에서 사용
    private Integer matchCount;
    private List<String> commonInterests;

    // 기술 스택 기반 매칭에서 사용
    private Integer stackMatchCount;
    private List<String> commonStacks;

    // 신규 회원 조회에서 사용
    private LocalDateTime joinedDate;
}