package com.virtukch.nest.project_application.dto;

import com.virtukch.nest.project_member.model.ProjectMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "프로젝트 지원 요청 바디(JSON)")
public class ProjectApplicationRequestDto {
    @Schema(description = "경로 변수로 전달되므로 무시됩니다.")
    private Long projectId;

    @Schema(description = "지원 파트", example = "BACKEND")
    private ProjectMember.Part part;
}
