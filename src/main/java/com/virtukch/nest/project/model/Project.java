package com.virtukch.nest.project.model;

import com.virtukch.nest.common.model.BaseTimeEntity;
import com.virtukch.nest.project.exception.InvalidProjectTitleException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId; //프로젝트 아이디

    private Long memberId;

    //프로젝트 제목
    private String projectTitle;

    //프로젝트 내용 -> 프로젝트 설명
    @Lob
    private String projectDescription;


    //모집 마감 여부 (2025.05.02 UPDATE)
    @Column(nullable = false)
    private Boolean isRecruiting = true;

    //조회수
    @Column(nullable = false)
    private Integer viewCount = 0;

    // image url
    @Column(columnDefinition = "TEXT")
    private String imageUrls;


    public static Project createProject(Long memberId,
                                        String projectTitle,
                                        String projectDescription) {
        if(projectTitle == null || projectTitle.isBlank()) {
            throw new InvalidProjectTitleException();
        }

        Project project = new Project();
        project.memberId = memberId;
        project.projectTitle = projectTitle;
        project.projectDescription = projectDescription;

        return project;
    }


    //조회수 증가 메서드
    public void incrementViewCount(){ this.viewCount++; }



    //프로젝트 업데이트 메서드 (부분 수정 규약)
    public void updateProject(String projectTitle,
                              String projectDescription,
                              Boolean isRecruiting) {
        // 제목: null/빈문자열이면 변경하지 않음, 값이 있으면 변경
        if (projectTitle != null) {
            if (!projectTitle.isBlank()) {
                this.projectTitle = projectTitle;
            } // 빈 문자열이면 변경하지 않음
        }

        // 설명: null이면 변경하지 않음, 빈 문자열이면 삭제, 값이 있으면 변경
        if (projectDescription != null) {
            if (projectDescription.isBlank()) {
                this.projectDescription = null; // 빈 문자열은 삭제 의미
            } else {
                this.projectDescription = projectDescription;
            }
        }

        // 모집여부: null이면 변경하지 않음, 값이 있으면 해당 값으로 설정
        if (isRecruiting != null) {
            this.isRecruiting = isRecruiting;
        }
    }

    public void updateProject(String projectTitle,
                              String projectDescription,
                              Boolean isRecruiting,
                              List<String> imageUrls) {
        updateProject(projectTitle, projectDescription,  isRecruiting);
        if(imageUrls != null) {
            this.imageUrls = imageUrls.isEmpty() ? null : String.join("||", imageUrls);
        }
    }

    public List<String> getImageUrlList() {
        if(imageUrls == null || imageUrls.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(imageUrls.split("\\|\\|"));
    }
}
