package ro.ubbcluj.cs.ams.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ActivitiesResponseDto {

    private List<ActivityResponseDto> activities;
}
