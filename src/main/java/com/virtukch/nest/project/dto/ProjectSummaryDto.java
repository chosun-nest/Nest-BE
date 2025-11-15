package com.virtukch.nest.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ProjectSummaryDto {
    private Long projectId;
    private String projectTitle;
    private String previewContent;
    private Integer currentNumberOfMembers;
    private Integer maximumNumberOfMembers;
    private Map<String, Integer> parts;
    private String creatorPart;
    private String creatorRole;
    private ProjectAuthorDto author;
    private List<String> tags;
    private Integer viewCount;
    private String createdAt;
    private Long commentCount;
    private String imageUrl;
    private Boolean isRecruiting;
}
