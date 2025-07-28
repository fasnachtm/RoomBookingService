package com.assessment.roombooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    @Schema(description = "room id", example = "1", required = true)
    private Long roomId;
    @Schema(description = "user name", example = "John Doe", required = true)
    private String userName;
    @Schema(description = "time in ISO format", example = "2025-12-31T10:00", required = true)
    private String startTime;
    @Schema(description = "time in ISO format", example = "2025-12-31T11:00", required = true)
    private String endTime;
}
