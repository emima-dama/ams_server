package ro.ubbcluj.cs.ams.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubjectsByStudentDto {

    private List<String> subjectsIds;
}
