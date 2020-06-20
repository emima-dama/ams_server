package ro.ubbcluj.cs.ams.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@NonNull
@Data
@Builder
public class GradesResponseDto {

    List<GradeResponseDto> grades;
}
