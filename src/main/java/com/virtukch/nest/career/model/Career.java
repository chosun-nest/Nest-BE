package com.virtukch.nest.career.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "career")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Career {
    @Id
    private Long careerId;
    private Long memberId;
    private String careerStartDate;
    private String careerEndDate;
    private String careerContent;
    private String careerCompanyName;
    private String careerPosition;
}
