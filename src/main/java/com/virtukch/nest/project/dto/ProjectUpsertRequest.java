package com.virtukch.nest.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.virtukch.nest.project_member.model.ProjectMember;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Schema(description = "프로젝트 생성/수정용 JSON 페이로드")
public class ProjectUpsertRequest {
    @NotBlank(message = "모집글 제목은 비어 있을 수 없습니다.")
    @Schema(description = "제목", example = "NEST 팀원 모집")
    private String projectTitle;

    @Schema(description = "설명(빈 문자열이면 삭제)", example = "커뮤니티 앱 백엔드 개발")
    private String projectDescription;

    @Schema(description = "모집 중 여부(null이면 미수정)", example = "true")
    private Boolean isRecruiting;

    @Schema(description = "태그 목록(null이면 미수정, []면 전부 제거)", example = "[\"UNCATEGORIZED\", \"SPRING\"]")
    private List<String> tags;

    @Schema(description = "역할별 정원 배열 구조")
    private List<ProjectSlotDto> slots;

    @Schema(description = "하위호환: map 구조도 허용(slots가 없을 때 사용)")
    @JsonProperty("parts")  // 프론트엔드에서 "parts"로 보내면 "partCounts"에 매핑
    private Map<ProjectMember.Part, Integer> partCounts;

    @Schema(description = "작성자 파트(슬롯에 포함되어야 함)", example = "BACKEND")
    private ProjectMember.Part creatorPart; // 작성자가 들어갈 파트

    @Schema(description = "작성자 역할", example = "LEADER")
    private ProjectMember.Role creatorRole = ProjectMember.Role.LEADER; // 기본값 LEADER

    @Schema(description = "제거할 멤버 ID 목록(선택)")
    private List<Long> membersToRemove;
}
