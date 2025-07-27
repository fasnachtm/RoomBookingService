package com.assessment.roombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    private Long roomId;
    private String userName;
    private String startTime;
    private String endTime;
}
