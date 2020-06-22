package ro.ubbcluj.cs.ams.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StudentsByGroupResponseDto {

    private List<String> usernames;
}
