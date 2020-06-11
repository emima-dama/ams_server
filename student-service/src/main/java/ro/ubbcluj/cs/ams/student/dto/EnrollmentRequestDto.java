package ro.ubbcluj.cs.ams.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnrollmentRequestDto {

    @NotNull(message = "studentUsername cannot be null")
    private String studentUsername;

    @NotNull(message = "subjectId cannot be null")
    private String subjectId;
}
