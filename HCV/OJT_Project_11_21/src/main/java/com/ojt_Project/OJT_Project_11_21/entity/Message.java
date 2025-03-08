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
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @Column(name = "message_sender_id")
    private int messageSenderId;

    @Column(name = "message_receiver_id")
    private int messageReceiverId;

    @Column(name = "messsage_context")
    private String messageContext;

    @Column(name = "message_send_time")
    private LocalDateTime messageSendTime;

    @Column(name = "message_image")
    private String messageImage;

}
