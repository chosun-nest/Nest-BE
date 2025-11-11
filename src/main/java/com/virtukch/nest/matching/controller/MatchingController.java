package com.virtukch.nest.matching.controller;

import com.virtukch.nest.auth.security.CustomUserDetails;
import com.virtukch.nest.matching.dto.MatchingSearchResponseDto;
import com.virtukch.nest.matching.dto.PopularMembersResponseDto;
import com.virtukch.nest.matching.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Tag(name = "Matching API", description = "매칭 서비스 API - 사용자 추천 및 검색")
public class MatchingController {

    private final MatchingService matchingService;

    @Operation(
            summary = "사용자 검색",
            description = """
                    이름, 전공, 학번으로 사용자를 검색합니다.

                    ✅ 검색 타입:
                    - `name`: 이름으로 검색
                    - `major`: 전공으로 검색
                    - `studentId`: 학번(이메일)으로 검색

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/search?keyword=김철수&type=name&page=0&size=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 페이지네이션:
                    - page: 페이지 번호 (0부터 시작, 기본값: 0)
                    - size: 페이지 크기 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = MatchingSearchResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 검색 타입"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/search")
    public ResponseEntity<MatchingSearchResponseDto> searchMembers(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword,
            @Parameter(description = "검색 타입 (name, major, studentId)", required = true)
            @RequestParam String type,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = user.getMember().getMemberId();
        MatchingSearchResponseDto response = matchingService.searchMembers(currentUserId, keyword, type, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "관심사 기반 매칭",
            description = """
                    현재 로그인한 사용자의 관심 태그와 1개 이상 겹치는 사람을 추천합니다.

                    ✅ 매칭 기준:
                    - 현재 사용자와 관심사가 1개 이상 겹치는 사용자
                    - 매칭된 관심사 개수와 공통 관심사 목록 포함

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/by-interests?page=0&size=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 페이지네이션:
                    - page: 페이지 번호 (0부터 시작, 기본값: 0)
                    - size: 페이지 크기 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 성공",
                    content = @Content(schema = @Schema(implementation = MatchingSearchResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/by-interests")
    public ResponseEntity<MatchingSearchResponseDto> findByCommonInterests(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = user.getMember().getMemberId();
        MatchingSearchResponseDto response = matchingService.findByCommonInterests(currentUserId, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "같은 학과 친구 추천",
            description = """
                    같은 전공 + 관심 태그 1개 이상 겹치는 사람을 추천합니다.

                    ✅ 매칭 기준:
                    - 현재 사용자와 같은 학과(전공)
                    - 관심사가 1개 이상 겹치는 사용자
                    - 매칭된 관심사 개수와 공통 관심사 목록 포함

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/same-major?page=0&size=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 페이지네이션:
                    - page: 페이지 번호 (0부터 시작, 기본값: 0)
                    - size: 페이지 크기 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 성공",
                    content = @Content(schema = @Schema(implementation = MatchingSearchResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/same-major")
    public ResponseEntity<MatchingSearchResponseDto> findBySameMajorAndCommonInterests(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = user.getMember().getMemberId();
        MatchingSearchResponseDto response = matchingService.findBySameMajorAndCommonInterests(currentUserId, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "인기 사용자 추천",
            description = """
                    팔로워 수가 많은 상위 사용자를 추천합니다.

                    ✅ 추천 기준:
                    - 팔로워 수 기준 내림차순 정렬
                    - 상위 N명의 인기 사용자

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/popular?limit=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 파라미터:
                    - limit: 조회할 사용자 수 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천 성공",
                    content = @Content(schema = @Schema(implementation = PopularMembersResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/popular")
    public ResponseEntity<PopularMembersResponseDto> findPopularMembers(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "조회할 사용자 수")
            @RequestParam(defaultValue = "20") int limit) {

        Long currentUserId = user.getMember().getMemberId();
        PopularMembersResponseDto response = matchingService.findPopularMembers(currentUserId, limit);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "기술 스택 기반 매칭",
            description = """
                    나의 기술 스택과 1개 이상 겹치는 사람을 추천합니다.

                    ✅ 매칭 기준:
                    - 현재 사용자와 기술 스택이 1개 이상 겹치는 사용자
                    - 매칭된 기술 스택 개수와 공통 기술 스택 목록 포함

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/by-tech-stack?page=0&size=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 페이지네이션:
                    - page: 페이지 번호 (0부터 시작, 기본값: 0)
                    - size: 페이지 크기 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 성공",
                    content = @Content(schema = @Schema(implementation = MatchingSearchResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/by-tech-stack")
    public ResponseEntity<MatchingSearchResponseDto> findByCommonTechStacks(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = user.getMember().getMemberId();
        MatchingSearchResponseDto response = matchingService.findByCommonTechStacks(currentUserId, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "신규 회원 조회",
            description = """
                    최근 N일 이내 가입한 신규 회원을 조회합니다.

                    ✅ 조회 기준:
                    - 가입일 기준 최근 N일 이내 가입자
                    - 가입일 최신순으로 정렬
                    - 가입일(joinedDate) 정보 포함

                    ✅ 요청 방법:
                    - HTTP Method: `GET`
                    - 요청 URL: `/api/v1/matching/new-members?days=7&page=0&size=20`
                    - 헤더: `Authorization: Bearer {access_token}`

                    ✅ 파라미터:
                    - days: 며칠 이내 가입자 (기본값: 7)
                    - page: 페이지 번호 (기본값: 0)
                    - size: 페이지 크기 (기본값: 20)
                    """,
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MatchingSearchResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/new-members")
    public ResponseEntity<MatchingSearchResponseDto> findNewMembers(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "며칠 이내 가입자")
            @RequestParam(defaultValue = "7") int days,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = user.getMember().getMemberId();
        MatchingSearchResponseDto response = matchingService.findNewMembers(currentUserId, days, page, size);
        return ResponseEntity.ok(response);
    }
}