package com.virtukch.nest.project.controller;

import com.virtukch.nest.auth.security.CustomUserDetails;
import com.virtukch.nest.project.dto.*;
import com.virtukch.nest.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "[프로젝트 모집 게시판] 게시글 API", description = "게시글 CRUD 등 게시판 관련 API\n문의 : dlwlgur02@gmail.com")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @Operation(
        summary = "프로젝트 모집글 생성",
        description = """
            새로운 프로젝트 모집글을 생성합니다.

            ## 요청 필드
            - `projectTitle`: 프로젝트 제목 (필수, 빈 문자열 또는 null 값 불가)
            - `projectDescription`: 프로젝트 상세 설명 (선택, null 값은 빈 문자열로 간주)
            - `tags`: 태그 목록 (선택, 태그 목록에 존재하는 태그만 설정 가능)
            - `parts`: 모집 역할 및 인원 리스트 (필수, Map<String, Integer> 형태)
                - 예: {"FRONTEND": 2, "BACKEND": 1, "PM": 1}
                - 가능한 역할(enum): BACKEND, FRONTEND, PM, DESIGN, AI, ETC

            ## 제약 조건
            ✔️ 로그인된 사용자만 작성 가능
            ✔️ 프로젝트 제목은 필수 입력 사항
            ✔️ 모집 역할 및 인원은 최소 1개 이상 필요
            ✔️ 성공 시 생성된 게시글의 URI를 Location 헤더로 반환

            ## 응답
            - 201 Created: 생성 성공
            - 400 Bad Request: 잘못된 요청 (제목 누락, 잘못된 태그 등)
            - 401 Unauthorized: 인증되지 않은 사용자
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PostMapping("/new")
    public ResponseEntity<ProjectResponseDto> createProject(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProjectRequestDto requestDTO) {
        Long memberId = user.getMember().getMemberId();
        log.info("[모집글 작성 요청] memberId={}", memberId);
        ProjectResponseDto responseDto = projectService.createProject(memberId, requestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/projects/" + responseDto.getProjectId()))
                .body(responseDto);
    }

    // 전체 프로젝트 조회
    @Operation(
        summary = "프로젝트 모집글 상세 조회",
        description = """
            특정 프로젝트 모집글의 상세 정보를 조회합니다.

            ## 요청 파라미터
            - `projectId`: 조회할 프로젝트의 고유 ID (경로 변수)

            ## 응답 정보
            - 프로젝트 기본 정보 (제목, 설명, 작성일, 수정일)
            - 프로젝트 작성자 정보 (이름, 학과, 학번)
            - 모집 역할별 정보 (역할명, 모집 인원, 현재 인원)
            - 현재 프로젝트 멤버 목록
            - 프로젝트 태그 목록
            - 조회수 정보

            ## 응답 코드
            - 200 OK: 조회 성공
            - 404 Not Found: 존재하지 않는 프로젝트
            """
    )
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponseDto> getProjectDetail(@PathVariable Long projectId) {
        ProjectDetailResponseDto responseDto = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "전체 프로젝트 모집글 조회",
        description = """
            모든 프로젝트 모집글을 페이지네이션으로 조회합니다. (이 기능은 Postman을 이용하여 테스트하는 것을 추천)

            ## 태그 필터링
            - 태그 필터링을 하지 않으면 전체 프로젝트를 반환합니다.
            - 태그를 필터링하려면 `?tags=JAVA&tags=SPRING`과 같이 쿼리 파라미터로 전달하세요.
            - 사용 가능한 태그: JAVA, SPRING, REACT, VUE, ANGULAR, NODE_JS, PYTHON, C, CPP, JAVASCRIPT 등

            ## 페이지네이션
            - 페이지 번호: `?page=0` (기본값: 0, 첫 페이지)
            - 페이지 크기: `?size=10` (기본값: 10, 페이지당 10개 항목)
            - 전체 예시: `?page=0&size=10`

            ## 정렬
            - 단일 필드 정렬: `?sort=createdAt,desc` (기본값: createdAt,desc)
            - 다중 필드 정렬: `?sort=viewCount,desc&sort=createdAt,desc`
            - 사용 가능한 정렬 필드: createdAt, viewCount, projectTitle

            ## 응답 정보
            - 프로젝트 목록 (제목, 설명, 작성자, 태그, 모집 현황)
            - 페이지 정보 (현재 페이지, 총 페이지, 총 항목 수)

            ## 전체 사용 예시
            - `/api/v1/projects?page=0&size=10&sort=createdAt,desc&tags=JAVA&tags=SPRING`

            ✔️ 태그가 없으면 전체 프로젝트 반환
            ✔️ 삭제된 프로젝트는 조회되지 않음
            """
    )
    @GetMapping
    public ResponseEntity<ProjectListResponseDto> getProjects(
            @RequestParam(required = false) List<String> tags,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ProjectListResponseDto responseDto;
        if(tags == null || tags.isEmpty()) {
            responseDto = projectService.getProjectList(pageable);
        } else {
            responseDto = projectService.getProjectList(tags, pageable);
        }
        return ResponseEntity.ok(responseDto);
    }


    //프로젝트 업데이트
    @Operation(
            summary = "프로젝트 모집글 수정",
            description = """
        기존 프로젝트 모집글의 내용을 수정합니다.
        PATCH 요청 시, 각 JSON 필드의 처리 방식은 다음과 같습니다:

        ## 요청 필드 처리 방식

        📌 projectTitle
        - "projectTitle": null 또는 생략 → 제목 수정하지 않음
        - "projectTitle": "" (빈 문자열) → 수정하지 않음 ***특히 주의***
        - "projectTitle": "새 제목" → 제목 수정

        📌 projectDescription
        - "projectDescription": null 또는 생략 → 설명 수정하지 않음
        - "projectDescription": "" → 프로젝트 설명을 전부 삭제
        - "projectDescription": "새 설명" → 설명 수정

        📌 tags
        - "tags": null 또는 생략 → 태그 수정하지 않음
        - "tags": [] → 태그 전부 제거
        - "tags": ["JAVA", "SPRING"] → 태그 재설정

        📌 parts
        - "parts": null 또는 생략 → 모집 인원 수정하지 않음
        - "parts": {} → 모든 모집 역할 제거 (주의: 프로젝트에 최소 1개 역할은 필요)
        - "parts": {"FRONTEND": 2, "BACKEND": 1} → 모집 역할 재설정

        ## 권한 및 제약 조건
        ✔️ 작성자 본인만 수정 가능
        ✔️ 이미 참여한 멤버가 있는 역할의 인원을 현재 멤버 수보다 적게 설정할 수 없음
        ✔️ 프로젝트에는 최소 1개의 모집 역할이 필요

        ## 응답 코드
        - 200 OK: 수정 성공
        - 400 Bad Request: 잘못된 요청 데이터
        - 401 Unauthorized: 인증되지 않은 사용자
        - 403 Forbidden: 수정 권한 없음 (작성자가 아님)
        - 404 Not Found: 존재하지 않는 프로젝트
        """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDto requestDto) {

        Long memberId = user.getMember().getMemberId();
        log.info("[모집글 수정 요청] projectId={}, memberId={}", projectId, memberId);
        ProjectResponseDto responseDto = projectService.updateProject(projectId, memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "프로젝트 모집글 삭제",
        description = """
            프로젝트 모집글을 삭제합니다.

            ## 요청 파라미터
            - `projectId`: 삭제할 프로젝트의 고유 ID (경로 변수)

            ## 삭제 조건 및 제약
            ✔️ 작성자 본인만 삭제 가능
            ✔️ 프로젝트에 참여 중인 멤버가 있는 경우 삭제 불가
            ✔️ 처리 대기 중인 지원서가 있는 경우 삭제 불가

            ## 삭제 후 처리
            - 삭제된 프로젝트는 조회할 수 없습니다
            - 관련된 프로젝트 태그 정보도 함께 삭제됩니다
            - 프로젝트 관련 지원서 정보도 함께 삭제됩니다

            ## 응답 코드
            - 200 OK: 삭제 성공
            - 401 Unauthorized: 인증되지 않은 사용자
            - 403 Forbidden: 삭제 권한 없음 (작성자가 아님)
            - 404 Not Found: 존재하지 않는 프로젝트
            - 409 Conflict: 삭제 불가능한 상태 (참여 멤버 존재 등)
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> deleteProject(@AuthenticationPrincipal CustomUserDetails user,
                                                            @PathVariable Long projectId) {
        Long memberId = user.getMember().getMemberId();
        log.info("[모집글 삭제 요청] projectId={}, memberId={}", projectId, memberId);
        ProjectResponseDto responseDto = projectService.deleteProject(projectId, memberId);
        return ResponseEntity.ok(responseDto);
    }


    @Operation(
        summary = "프로젝트 모집글 검색",
        description = """
            키워드를 사용하여 프로젝트 모집글을 검색합니다.

            ## 검색 키워드
            - `keyword`: 검색할 키워드 (필수)
            - 공백이 포함된 키워드도 검색 가능

            ## 검색 타입
            - `searchType`: 검색 범위 지정 (선택, 기본값: ALL)
                - ALL: 제목, 설명, 작성자 모두에서 검색 (기본값)
                - TITLE: 제목에서만 검색
                - CONTENT: 설명에서만 검색
                - AUTHOR: 작성자 이름에서만 검색

            ## 태그 필터링
            - 태그 필터링을 추가하려면 `?tags=JAVA&tags=SPRING`과 같이 전달하세요
            - 검색 결과에서 특정 기술 스택으로 한번 더 필터링 가능

            ## 페이지네이션
            - 페이지 번호: `?page=0` (기본값: 0, 첫 페이지)
            - 페이지 크기: `?size=10` (기본값: 10, 페이지당 10개 항목)

            ## 정렬
            - 단일 필드 정렬: `?sort=createdAt,desc` (기본값: createdAt,desc)
            - 다중 필드 정렬: `?sort=viewCount,desc&sort=createdAt,desc`
            - 사용 가능한 정렬 필드: createdAt, viewCount, projectTitle

            ## 검색 결과
            - 키워드와 일치하는 프로젝트 목록
            - 각 결과는 기본 프로젝트 정보 (제목, 설명, 작성자, 태그, 모집 현황) 포함

            ## 전체 사용 예시
            - `/api/v1/projects/search?keyword=스프링&searchType=TITLE&page=0&size=10&sort=createdAt,desc&tags=JAVA`
            - `/api/v1/projects/search?keyword=웹 개발&searchType=ALL&tags=REACT&tags=NODE_JS`
            - `/api/v1/projects/search?keyword=홍길동&searchType=AUTHOR&page=0&size=10`

            ## 응답 코드
            - 200 OK: 검색 성공 (결과가 없어도 200 반환)
            - 400 Bad Request: 키워드가 누락되거나 잘못된 검색 타입
            """
    )
    @GetMapping("/search")
    public ResponseEntity<ProjectListResponseDto> searchProjects(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "ALL") String searchType,
            @RequestParam(required = false) List<String> tags,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ProjectListResponseDto responseDto;
        if(tags == null || tags.isEmpty()) {
            responseDto = projectService.searchProjects(keyword, searchType, pageable);
        } else {
            responseDto = projectService.searchProjectsWithTags(keyword, tags, searchType, pageable);
        }

        return ResponseEntity.ok(responseDto);
    }

}
