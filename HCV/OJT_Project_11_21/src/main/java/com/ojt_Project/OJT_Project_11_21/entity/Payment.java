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
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_bank")
    private String paymentBank;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
