package ro.ubbcluj.cs.ams.subject.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class SubjectRequestDto {

    @NotNull
    private String name;
    @NotNull
    private Integer credits;
    @NotNull
    private Integer specId;
    @NotNull
    private Integer year;
}

