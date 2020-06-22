package ro.ubbcluj.cs.ams.attendance.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendanceByTeacherDto {

    Integer id;
    String course; //id
    Integer activity; //id
    String professor;
    String date;
}
