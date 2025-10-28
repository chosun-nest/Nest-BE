package com.virtukch.nest.project.dto;

import com.virtukch.nest.project_member.model.ProjectMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "역할별 정원 정보")
public class ProjectSlotDto {
    @Schema(description = "역할", example = "BACKEND")
    private ProjectMember.Part part;

    @Schema(description = "정원(빈 슬롯 포함)", example = "2")
    private Integer count;
}
