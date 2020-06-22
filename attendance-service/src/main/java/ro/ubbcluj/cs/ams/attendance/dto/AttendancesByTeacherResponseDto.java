package ro.ubbcluj.cs.ams.attendance.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendancesByTeacherResponseDto {

    List<AttendanceByTeacherDto> attendances;
}
