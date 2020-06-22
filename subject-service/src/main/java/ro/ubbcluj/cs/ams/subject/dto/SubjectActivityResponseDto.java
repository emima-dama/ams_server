package ro.ubbcluj.cs.ams.subject.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class SubjectActivityResponseDto {

    String subjectName;
    String activityName;

}
