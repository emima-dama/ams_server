package ro.ubbcluj.cs.ams.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SubjectResponseDto {

    String subjectName;
    String activityName;
}

