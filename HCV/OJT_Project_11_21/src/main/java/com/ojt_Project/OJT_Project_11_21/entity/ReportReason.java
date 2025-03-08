package com.ojt_Project.OJT_Project_11_21.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ReportReason")
public class ReportReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportReason_id")
    private int reportReasonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id",nullable = true)
    private Report report;

    @Column(name = "reportReason_context")
    private String reportReasonContext;

}
