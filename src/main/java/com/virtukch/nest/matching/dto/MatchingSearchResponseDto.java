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
public class MatchingSearchResponseDto {

    private List<MatchingMemberResponseDto> members;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
}