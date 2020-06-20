package ro.ubbcluj.cs.ams.attendance.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendanceResponse {

    private String course;
    private String activity;
}
