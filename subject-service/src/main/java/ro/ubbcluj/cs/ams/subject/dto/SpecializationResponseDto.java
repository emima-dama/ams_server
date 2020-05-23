package ro.ubbcluj.cs.ams.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpecializationResponseDto {

    private Integer id;
    private String name;
    private String language;
}
