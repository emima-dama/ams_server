package ro.ubbcluj.cs.ams.subject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Data
public class SubjectResponseDto {

    private String id;
    private String name;
    private Integer credits;
    private Integer specId;
    private Integer year;
}
