package com.example.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiry_response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enquiry_id", nullable = false)
    private Enquiry enquiry;

    @ManyToOne
    @JoinColumn(name = "responder_id", nullable = false)
    private Staff responder;

    @Column(name = "response_message", nullable = false)
    private String responseMessage;

    @Column(name = "responded_at", nullable = false)
    private LocalDateTime respondedAt;
}
