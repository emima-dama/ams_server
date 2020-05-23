package ro.ubbcluj.cs.ams.attendance.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendanceInfoReq {

    @NotNull(message = "Course id cannot be null!")
    private String courseId;
    @NotNull(message = "Activity id cannot be null!")
    private Integer activityId;
    @NotNull(message = "Remaining Time cannot be null")
    private Integer remainingTime;
}
