package com.virtukch.nest.project_member.controller;


import com.virtukch.nest.project_member.model.ProjectMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
@Tag(name = "[프로젝트 모집 게시판] 역할 API", description = "프로젝트 참여 역할 관련 API\n문의 : dlwlgur02@gmail.com")
@RestController
@RequestMapping("/api/v1/projects/members")
@RequiredArgsConstructor
public class ProjectMemberController {
    @Operation(
        summary = "프로젝트 참여 역할 목록 조회",
        description = """
            프로젝트 참여 시 선택 가능한 모든 역할(Enum) 목록을 조회합니다.

            ## 사용 목적
            - 프로젝트 생성 시 모집 역할 선택
            - 프로젝트 지원 시 역할 선택
            - 클라이언트 사이드 드롭다운/선택 UI 구성

            ## 응답 데이터
            사용 가능한 모든 프로젝트 참여 역할(enum)을 문자열 배열로 반환:
            - BACKEND
            - FRONTEND
            - PM
            - DESIGN
            - AI
            - ETC

            ## 응답 예시
            ```json
            ["BACKEND", "FRONTEND", "PM", "DESIGN", "AI", "ETC"]
            ```

            ## 응답 코드
            - 200 OK: 조회 성공
            - 401 Unauthorized: 인증되지 않은 사용자

            ✔️ 정적 데이터이므로 캐싱 가능
            ✔️ 새로운 역할 추가 시 서버 재배포 필요
            """,
        security = {@SecurityRequirement(name = "bearer-key")}
    )
    @GetMapping
    public ResponseEntity<List<String>> getPartEnums() {
        List<String> parts = Arrays.stream(ProjectMember.Part.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(parts);
    }
}
