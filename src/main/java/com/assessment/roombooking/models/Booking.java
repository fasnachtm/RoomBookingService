package com.assessment.roombooking.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "booking", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to the room
    private Long roomId;

    private String userName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Booking(Long roomId, String userName, LocalDateTime startTime, LocalDateTime endTime) {
        this.roomId = roomId;
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
