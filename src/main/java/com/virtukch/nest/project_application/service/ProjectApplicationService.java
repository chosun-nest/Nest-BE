package com.virtukch.nest.project_application.service;

import com.virtukch.nest.common.model.BaseTimeEntity;
import com.virtukch.nest.project_application.exception.ProjectNotFoundException;
import com.virtukch.nest.project_application.dto.ProjectApplicationRequestDto;
import com.virtukch.nest.project_application.dto.ProjectApplicationResponseDto;
import com.virtukch.nest.project_application.dto.converter.ProjectApplicationDtoConverter;
import com.virtukch.nest.project_application.exception.*;
import com.virtukch.nest.project_application.model.ProjectApplication;
import com.virtukch.nest.project_application.repository.ProjectApplicationRepository;
import com.virtukch.nest.member.model.Member;
import com.virtukch.nest.member.repository.MemberRepository;
import com.virtukch.nest.project.repository.ProjectRepository;
import com.virtukch.nest.project_member.model.ProjectMember;
import com.virtukch.nest.project_member.repository.ProjectMemberRepository;
import com.virtukch.nest.project_application.exception.AlreadyProcessedApplicationException;
import com.virtukch.nest.project.model.Project;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectApplicationService extends BaseTimeEntity {

    private final ProjectApplicationRepository projectApplicationRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public ProjectApplicationResponseDto applyToProject(Long projectId, Long memberId, ProjectApplicationRequestDto requestDto) {
        return applyToProject(projectId, memberId, requestDto.getPart());
    }

    @Transactional
    public ProjectApplicationResponseDto applyToProject(Long projectId, Long memberId, com.virtukch.nest.project_member.model.ProjectMember.Part part) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (project.getMemberId().equals(memberId)) {
            throw new ProjectOwnerCannotApplyException(); // 또는 IllegalStateException
        }

        // 모집 중이 아닌 경우 지원 불가
        if (Boolean.FALSE.equals(project.getIsRecruiting())) {
            throw new ProjectFullException("현재 모집 중이 아닙니다.");
        }

        // 이미 프로젝트 멤버인 경우 지원 불가
        if (projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).isPresent()) {
            throw new DuplicateApplicationException();
        }

        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // 중복 지원 방지 (REJECTED 또는 CANCELLED 제외)
        if (projectApplicationRepository.existsByProjectIdAndMemberIdAndStatusNotIn(
                projectId,
                memberId,
                List.of(ProjectApplication.ApplicationStatus.REJECTED, ProjectApplication.ApplicationStatus.CANCELED))) {
            throw new DuplicateApplicationException();
        }

        // 파트 정원 초과 시 즉시 차단 (빈 슬롯/정원 확인)
        long maxCount = projectMemberRepository.countByProjectIdAndPart(projectId, part);
        long approvedCount = projectMemberRepository.countByProjectIdAndPartAndMemberIdIsNotNull(projectId, part);
        if (approvedCount >= maxCount) {
            throw new ProjectFullException(part + " 파트의 모집 인원이 가득 찼습니다.");
        }

        ProjectApplication application = ProjectApplication.builder()
                .projectId(projectId)
                .memberId(memberId)
                .part(part)
                .status(ProjectApplication.ApplicationStatus.WAITING)
                .appliedAt(LocalDateTime.now())
                .build();

        projectApplicationRepository.save(application);

        Member member = memberRepository.findById(memberId).orElse(null);

        return ProjectApplicationDtoConverter.toResponseDto(application, member != null ? member.getMemberName() : "알 수 없음");
    }

    @Transactional
    public List<ProjectApplicationResponseDto> getApplicationsByProject(Long projectId, Long requesterId) {
        // 프로젝트 소유자만 조회 가능
        Long writerId = projectRepository.searchWriterIdByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (!writerId.equals(requesterId)) {
            throw new NotProjectOwnerException();
        }

        List<ProjectApplication> applications = projectApplicationRepository.findByProjectId(projectId);

        return applications.stream().map(app -> {
            Member member = memberRepository.findById(app.getMemberId()).orElse(null);
            return ProjectApplicationDtoConverter.toResponseDto(app, member != null ? member.getMemberName() : "알 수 없음");
        }).toList();
    }

    @Transactional
    public ProjectApplicationResponseDto acceptApplication(Long projectId, Long applicationId, Long requesterId) {
        ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);
        validateOwnership(projectId, requesterId, application);
        if (application.getStatus() != ProjectApplication.ApplicationStatus.WAITING) {
            throw new AlreadyProcessedApplicationException();
        }

        // 현재 ACCEPTED 상태의 인원 수 조회
        long acceptedCount = projectApplicationRepository.countByProjectIdAndStatus(
                projectId, ProjectApplication.ApplicationStatus.ACCEPTED);

        // 프로젝트 최대 인원 수를 동적으로 계산
        int maxMember = projectMemberRepository.findByProjectId(projectId).size();

        if (acceptedCount >= maxMember) {
            throw new ProjectFullException("프로젝트 모집 인원을 초과하여 승인할 수 없습니다.");
        }

        application.setStatus(ProjectApplication.ApplicationStatus.ACCEPTED);
        projectApplicationRepository.save(application);

        List<ProjectMember> vacantList = projectMemberRepository
                .findByProjectIdAndPartAndMemberIdIsNull(projectId, application.getPart());

        if (!vacantList.isEmpty()) {
            ProjectMember vacant = vacantList.get(0);
            vacant.setMemberId(application.getMemberId());
            projectMemberRepository.save(vacant);
        } else {
            // 파트별 모집 인원 초과 방지
            long maxCount = projectMemberRepository.countByProjectIdAndPart(projectId, application.getPart());
            long approvedCount = projectMemberRepository.countByProjectIdAndPartAndMemberIdIsNotNull(projectId, application.getPart());
            if (approvedCount >= maxCount) {
                throw new ProjectFullException(application.getPart() + " 파트의 모집 인원을 초과할 수 없습니다.");
            }
            projectMemberRepository.save(ProjectMember.builder()
                    .projectId(projectId)
                    .memberId(application.getMemberId())
                    .role(ProjectMember.Role.MEMBER)
                    .part(application.getPart())
                    .build());
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        int registeredCount = projectMemberRepository
                .findByProjectIdAndMemberIdIsNotNull(projectId)
                .size();

        if (registeredCount >= maxMember) {
            project.setIsRecruiting(false);
            projectRepository.save(project);
        }

        Member member = memberRepository.findById(application.getMemberId()).orElse(null);
        return ProjectApplicationDtoConverter.toResponseDto(application, member != null ? member.getMemberName() : "알 수 없음");
    }

    @Transactional
    public ProjectApplicationResponseDto rejectApplication(Long projectId, Long applicationId, Long requesterId) {
        ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);
        validateOwnership(projectId, requesterId, application);
        if (application.getStatus() != ProjectApplication.ApplicationStatus.WAITING) {
            throw new AlreadyProcessedApplicationException();
        }

        application.setStatus(ProjectApplication.ApplicationStatus.REJECTED);
        projectApplicationRepository.save(application);

        Member member = memberRepository.findById(application.getMemberId()).orElse(null);
        return ProjectApplicationDtoConverter.toResponseDto(application, member != null ? member.getMemberName() : "알 수 없음");
    }

    @Transactional
    public List<ProjectApplicationResponseDto> getApplicationsByMember(Long memberId) {
        List<ProjectApplication> applications = projectApplicationRepository.findByMemberId(memberId);
        return applications.stream().map(app -> {
            Member member = memberRepository.findById(app.getMemberId()).orElse(null);
            return ProjectApplicationDtoConverter.toResponseDto(app, member != null ? member.getMemberName() : "알 수 없음");
        }).toList();
    }

    @Transactional
    public ProjectApplicationResponseDto cancelApplication(Long projectId, Long applicationId, Long requesterId) {
        ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);
        if (!application.getProjectId().equals(projectId)) {
            throw new ApplicationNotFoundException();
        }
        if (!application.getMemberId().equals(requesterId)) {
            throw new NotProjectOwnerException(); // 본인만 취소 가능 (권한 예외 재사용)
        }
        if (application.getStatus() != ProjectApplication.ApplicationStatus.WAITING) {
            throw new AlreadyProcessedApplicationException();
        }
        application.setStatus(ProjectApplication.ApplicationStatus.CANCELED);
        projectApplicationRepository.save(application);
        Member member = memberRepository.findById(application.getMemberId()).orElse(null);
        return ProjectApplicationDtoConverter.toResponseDto(application, member != null ? member.getMemberName() : "알 수 없음");
    }

    private void validateOwnership(Long projectId, Long requesterId, ProjectApplication application) {
        if (!application.getProjectId().equals(projectId)) {
            throw new ApplicationNotFoundException();
        }

        Long writerId = projectRepository.searchWriterIdByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!writerId.equals(requesterId)) {
            throw new NotProjectOwnerException();
        }
    }
}
