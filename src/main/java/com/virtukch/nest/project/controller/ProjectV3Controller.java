package com.virtukch.nest.project.controller;

import com.virtukch.nest.auth.security.CustomUserDetails;
import com.virtukch.nest.project.dto.ProjectResponseDto;
import com.virtukch.nest.project.dto.ProjectUpsertRequest;
import com.virtukch.nest.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/projects")
@Tag(name = "[프로젝트 모집 게시판 v3] 게시글 API", description = "payload(JSON) + images(multipart) 구조의 간소화된 API")
public class ProjectV3Controller {
    private final ProjectService projectService;

    @Operation(
        summary = "프로젝트 모집글 생성 (payload + images)",
        description = """
            multipart/form-data로 JSON `payload`와 파일 배열 `images`를 함께 전송합니다.
            
            - payload(JSON): ProjectUpsertRequest 스키마 참고
            - images(File[]) : 선택, 여러 개 첨부 가능 (전체 교체 기준)
            - consumes: multipart/form-data
            
            예시 (form-data):
            - payload: {
                "projectTitle":"NEST 팀원 모집(v3)",
                "projectDescription":"payload + images",
                "isRecruiting":true,
                "tags":["UNCATEGORIZED"],
                "slots":[{"part":"BACKEND","count":2},{"part":"FRONTEND","count":2}],
                "creatorPart":"BACKEND"
              }
            - images: file1, file2 ...
            
            응답: 201 Created + ProjectResponseDto (Location 헤더 포함)
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDto> createProject(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("payload") ProjectUpsertRequest payload,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Long memberId = user.getMember().getMemberId();
        log.info("[모집글 생성 v3 요청] memberId={}", memberId);
        ProjectResponseDto responseDto = projectService.createProject(memberId, payload, images);
        return ResponseEntity
                .created(URI.create("/api/v3/projects/" + responseDto.getProjectId()))
                .body(responseDto);
    }

    @Operation(
        summary = "프로젝트 모집글 수정 (payload + images)",
        description = """
            multipart/form-data로 JSON `payload`와 파일 배열 `images`를 함께 전송합니다.
            
            - payload(JSON): null 필드는 미수정, 설명 빈 문자열은 삭제
            - images(File[]): 미포함 시 기존 유지, 포함 시 전체 교체
            
            응답: 200 OK + ProjectResponseDto
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PatchMapping(path = "/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDto> updateProject(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @RequestPart("payload") ProjectUpsertRequest payload,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Long memberId = user.getMember().getMemberId();
        log.info("[모집글 수정 v3 요청] projectId={}, memberId={}", projectId, memberId);
        ProjectResponseDto responseDto = projectService.updateProject(projectId, memberId, payload, images);
        return ResponseEntity.ok(responseDto);
    }
}
