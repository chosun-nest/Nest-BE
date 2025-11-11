package com.virtukch.nest.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PopularMembersResponseDto {

    private List<MatchingMemberResponseDto> members;
}