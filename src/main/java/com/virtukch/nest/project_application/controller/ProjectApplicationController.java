package com.virtukch.nest.project_application.controller;

import com.virtukch.nest.auth.security.CustomUserDetails;
import com.virtukch.nest.project_application.dto.ProjectApplicationRequestDto;
import com.virtukch.nest.project_application.dto.ProjectApplicationResponseDto;
import com.virtukch.nest.project_application.service.ProjectApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@Tag(name = "[프로젝트 모집글 지원 관리] 지원 처리 API", description = "프로젝트 모집글에 지원하고, 지원 현황(지원자 목록 등)을 조회할 수 있는 API입니다.\n문의 : dlwlgur02@gmail.com")
public class ProjectApplicationController {
    private final ProjectApplicationService projectApplicationService;

    @Operation(
        summary = "프로젝트 모집글에 지원",
        description = """
            로그인된 사용자가 해당 프로젝트 모집글에 지원합니다.

            ## 요청 파라미터
            - `projectId`: 지원할 프로젝트의 고유 ID (경로 변수)

            ## 요청 필드
            - `part`: 지원하려는 역할 (필수)
                - 사용 가능한 역할: FRONTEND, BACKEND, DESIGNER, PLANNER, DEVOPS, FULLSTACK, ANDROID, IOS
            - `message`: 지원 메시지/자기소개 (선택)
            - `portfolio`: 포트폴리오 URL (선택)

            ## 지원 조건 및 제약
            ✔️ 로그인된 사용자만 지원 가능
            ✔️ 같은 프로젝트에 중복 지원 불가능
            ✔️ 프로젝트 작성자는 자신의 프로젝트에 지원할 수 없음
            ✔️ 해당 역할의 모집 인원이 마감된 경우 지원 불가
            ✔️ 이미 해당 프로젝트의 멤버인 경우 지원 불가

            ## 지원 후 처리
            - 지원서 상태는 'PENDING'으로 설정
            - 프로젝트 작성자가 수락/거절할 때까지 대기

            ## 응답 코드
            - 200 OK: 지원 성공
            - 400 Bad Request: 잘못된 요청 (중복 지원, 잘못된 역할 등)
            - 401 Unauthorized: 인증되지 않은 사용자
            - 403 Forbidden: 지원 불가 상태 (프로젝트 작성자, 이미 멤버 등)
            - 404 Not Found: 존재하지 않는 프로젝트
            - 409 Conflict: 모집 마감 등의 이유로 지원 불가
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PostMapping("/{projectId}/apply")
    public ResponseEntity<ProjectApplicationResponseDto> projectApplicationApply(@AuthenticationPrincipal CustomUserDetails user,
                                      @PathVariable Long projectId,
                                      @RequestBody ProjectApplicationRequestDto requestDto) {
        Long memberId = user.getMember().getMemberId();
        ProjectApplicationResponseDto responseDto = projectApplicationService.applyToProject(projectId, memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "프로젝트 모집글 지원자 목록 조회",
        description = """
            특정 프로젝트에 지원한 모든 지원자의 목록을 조회합니다.

            ## 요청 파라미터
            - `projectId`: 지원자 목록을 조회할 프로젝트의 고유 ID (경로 변수)

            ## 조회 권한
            ✔️ 프로젝트 작성자만 조회 가능
            ✔️ 다른 사용자가 조회 시 403 Forbidden 반환

            ## 응답 정보
            각 지원자별로 다음 정보를 포함:
            - 지원자 기본 정보 (이름, 학과, 학번, 이메일)
            - 지원 내용 (지원 역할, 지원 메시지, 포트폴리오)
            - 지원 상태 (PENDING, ACCEPTED, REJECTED)
            - 지원 일시 및 처리 일시

            ## 지원 상태 종류
            - PENDING: 검토 대기 중
            - ACCEPTED: 수락됨 (프로젝트 멤버로 등록됨)
            - REJECTED: 거절됨

            ## 정렬
            - 지원 일시 기준 최신순으로 정렬
            - 상태별로는 PENDING → ACCEPTED → REJECTED 순으로 우선순위

            ## 응답 코드
            - 200 OK: 조회 성공 (지원자가 없어도 빈 배열로 200 반환)
            - 401 Unauthorized: 인증되지 않은 사용자
            - 403 Forbidden: 조회 권한 없음 (프로젝트 작성자가 아님)
            - 404 Not Found: 존재하지 않는 프로젝트
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @GetMapping("/{projectId}/applications")
    public ResponseEntity<List<ProjectApplicationResponseDto>> getProjectApplications(@AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId) {
        List<ProjectApplicationResponseDto> applications = projectApplicationService.getApplicationsByProject(projectId);
        return ResponseEntity.ok(applications);
    }


    @Operation(
        summary = "프로젝트 지원 수락",
        description = """
            프로젝트 작성자가 특정 지원자의 지원을 수락합니다.

            ## 요청 파라미터
            - `projectId`: 프로젝트의 고유 ID (경로 변수)
            - `applicationId`: 수락할 지원서의 고유 ID (경로 변수)

            ## 수락 조건 및 제약
            ✔️ 프로젝트 작성자만 수락 가능
            ✔️ PENDING 상태의 지원서만 수락 가능
            ✔️ 해당 역할의 모집 인원에 여유가 있어야 함
            ✔️ 이미 처리된 지원서(ACCEPTED/REJECTED)는 수락 불가

            ## 수락 후 처리
            - 지원자가 해당 프로젝트의 멤버로 자동 등록
            - 지원서 상태가 'ACCEPTED'로 변경
            - 해당 역할의 현재 인원수가 1 증가
            - 모집 인원이 가득 찬 경우, 동일 역할의 다른 PENDING 지원서들은 자동으로 REJECTED 처리

            ## 알림 처리
            - 수락된 지원자에게 알림 전송 (구현 시)
            - 거절된 다른 지원자들에게도 알림 전송 (모집 마감 시)

            ## 응답 코드
            - 200 OK: 수락 성공
            - 400 Bad Request: 이미 처리된 지원서 또는 잘못된 요청
            - 401 Unauthorized: 인증되지 않은 사용자
            - 403 Forbidden: 수락 권한 없음 (프로젝트 작성자가 아님)
            - 404 Not Found: 존재하지 않는 프로젝트 또는 지원서
            - 409 Conflict: 모집 인원 초과로 수락 불가
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PostMapping("/{projectId}/applications/{applicationId}/accept")
    public ResponseEntity<ProjectApplicationResponseDto> acceptApplication(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @PathVariable Long applicationId) {
        Long memberId = user.getMember().getMemberId();
        ProjectApplicationResponseDto responseDto = projectApplicationService.acceptApplication(projectId, applicationId, memberId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "프로젝트 지원 거절",
        description = """
            프로젝트 작성자가 특정 지원자의 지원을 거절합니다.

            ## 요청 파라미터
            - `projectId`: 프로젝트의 고유 ID (경로 변수)
            - `applicationId`: 거절할 지원서의 고유 ID (경로 변수)

            ## 거절 조건 및 제약
            ✔️ 프로젝트 작성자만 거절 가능
            ✔️ PENDING 상태의 지원서만 거절 가능
            ✔️ 이미 처리된 지원서(ACCEPTED/REJECTED)는 거절 불가

            ## 거절 후 처리
            - 지원서 상태가 'REJECTED'로 변경
            - 지원자의 프로젝트 멤버 등록은 이루어지지 않음
            - 해당 역할의 모집 인원에는 영향 없음

            ## 거절과 수락의 차이점
            - 거절: 단순히 지원서 상태만 변경
            - 수락: 지원자를 프로젝트 멤버로 등록 + 모집 인원 관리

            ## 알림 처리
            - 거절된 지원자에게 알림 전송 (구현 시)

            ## 응답 정보
            - 거절된 지원서의 최신 정보 반환
            - 거절 처리 일시 포함

            ## 응답 코드
            - 200 OK: 거절 성공
            - 400 Bad Request: 이미 처리된 지원서 또는 잘못된 요청
            - 401 Unauthorized: 인증되지 않은 사용자
            - 403 Forbidden: 거절 권한 없음 (프로젝트 작성자가 아님)
            - 404 Not Found: 존재하지 않는 프로젝트 또는 지원서
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @PostMapping("/{projectId}/applications/{applicationId}/reject")
    public ResponseEntity<ProjectApplicationResponseDto> rejectApplication(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @PathVariable Long applicationId) {
        Long memberId = user.getMember().getMemberId();
        ProjectApplicationResponseDto responseDto = projectApplicationService.rejectApplication(projectId, applicationId, memberId);
        return ResponseEntity.ok(responseDto);
    }

}
