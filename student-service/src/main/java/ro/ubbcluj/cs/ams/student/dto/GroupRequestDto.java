package ro.ubbcluj.cs.ams.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupRequestDto {

    @NotNull(message = "name cannot me null")
    private String name;

    @NotNull(message = "specId cannot be null and need to be min 1")
    @Min(1)
    private Integer specId;

    @NotNull
    @Min(1)
    private Integer year;
}
